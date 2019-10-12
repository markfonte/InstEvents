module.exports = function parseDate(dateString) {
  const parts = dateString.split('-').filter(value => value !== "");
  if(parts.length < 5) {
    throw new Error('Improperly formatted date. Must be formatted as YYYY-MM-DD-HH-MM.');
  }
  const [ year, month, day, hour, minute ] = parts;
  return new Date(Date.UTC(year, month-1, day, hour, minute));
};
