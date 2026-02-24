function safeParse(raw) {
  try {
    if (typeof raw !== "string") {
      return null;
    }
    return JSON.parse(raw);
  } catch {
    return null;
  }
}

module.exports = { safeParse };
