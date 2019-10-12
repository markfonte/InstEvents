const parseDate = require('./parseDate');

describe('parseDate', () => {

  it('should parse date correctly', () => {
    const expectedDate = new Date("2019-10-11T02:10:00Z");
    expect(parseDate('2019-10-11-02-10')).toEqual(expectedDate);
  });

  it('should throw an exception for improperly formatted date strings', () => {
    expect(() => parseDate('')).toThrow(Error);
    expect(() => parseDate('-')).toThrow(Error);
    expect(() => parseDate('2019')).toThrow(Error);
    expect(() => parseDate('2019-10')).toThrow(Error);
    expect(() => parseDate('2019-10-11')).toThrow(Error);
    expect(() => parseDate('2019-10-11-02')).toThrow(Error);
    expect(() => parseDate('2019-10-11-02-')).toThrow(Error);
  });

});
