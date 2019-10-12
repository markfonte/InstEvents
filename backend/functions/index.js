const functions = require('firebase-functions');
const webApi = require('./src/webApi');

exports.api = functions.https.onRequest(webApi.handler);
