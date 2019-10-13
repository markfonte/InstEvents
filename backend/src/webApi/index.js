const functions = require('firebase-functions');
const express = require('express');
const cors = require('cors');
const app = express();
const db = require('../db');
const parseDate = require('../util/parseDate');

app.use(cors({ origin: true }));
app.use(express.json());

app.get('/events', async (req, res) => {
  const events = db.collection('events');
  try {
    const eventsResults = {};
    const snapshot = await events.get();
    const startDate = req.query.start_date;
    const endDate = req.query.end_date;
    snapshot.forEach(doc => {
      let data = doc.data();
      data.start_date = data.start_date && data.start_date.toDate && data.start_date.toDate();
      data.end_date = data.end_date && data.end_date.toDate && data.end_date.toDate();
      // filter events that are not occurring during this time frame
      if(startDate && endDate && (data.start_date > parseDate(endDate) || data.end_date < parseDate(startDate))) {
        return;
      }
      eventsResults[doc.id] = data;
    });
    res.send(eventsResults);
  } catch (err) {
    res.send({ 'error': err.message });
  }
});

app.post('/events', async (req, res) => {
  const events = db.collection('events');
  const postData = req.body;
  const event = await events.add({
    ...postData,
    start_date: parseDate(postData.start_date),
    end_date: parseDate(postData.end_date),
  });
  res.send(event.id);
});

exports.handler = functions.https.onRequest(app);
