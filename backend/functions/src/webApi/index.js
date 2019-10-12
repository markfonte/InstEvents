const functions = require('firebase-functions');
const express = require('express');
const cors = require('cors');
const app = express();
const db = require('../db');

app.use(cors({ origin: true }));

app.get('/events', async (req, res) => {
  let query = db.collection('events');
  try {
    if (req.query.start_date) {
      query = query.where('date', '>', parseDate(req.query.start_date));
    }
    if (req.query.end_date) {
      query = query.where('date', '<', parseDate(req.query.end_date));
    }
    const eventsResults = {};
    const snapshot = await query.get();
    snapshot.forEach(doc => {
      let data = doc.data();
      data.date = data.date.toDate();
      eventsResults[doc.id] = data;
    });
    res.send(eventsResults);
  } catch (err) {
    res.send({ 'error': err.message });
  }
});

app.get('/hello', (req, res) => {
  res.end("Received GET request!");
});

function parseDate(dateString) {
  const parts = dateString.split('-');
  if(parts.length < 5) {
    throw new Error('Improperly formatted date. Must be formatted as YYYY-MM-DD-HH-MM.');
  }
  const [ year, month, day, hour, minute ] = parts;
  return new Date(year, month-1, day, hour, minute, 0, 0);
}

exports.handler = functions.https.onRequest(app);
