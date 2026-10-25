'use strict';

/**
 * Problem 1: first non-repeating character.
 * Counting is case-insensitive, but the original character is returned.
 * Decision: spaces and punctuation are treated like any other character
 * (they are counted and can be returned). See README.
 */
function firstNonRepeatingChar(str) {
  if (typeof str !== 'string') return null;
  const chars = Array.from(str); // handles characters outside the BMP (e.g. emoji)
  const counts = new Map();
  for (const ch of chars) {
    const key = ch.toLowerCase();
    counts.set(key, (counts.get(key) || 0) + 1);
  }
  for (const ch of chars) {
    if (counts.get(ch.toLowerCase()) === 1) return ch;
  }
  return null;
}

/**
 * Problem 2: two sum, one pass with a Map (value -> index). O(n) time, O(n) space.
 * For each number, check whether its complement was already seen.
 */
function findTwoSum(nums, target) {
  if (!Array.isArray(nums)) return null;
  if (typeof target !== 'number' || !Number.isFinite(target)) return null;

  const seen = new Map();
  for (let i = 0; i < nums.length; i++) {
    const num = nums[i];
    if (typeof num !== 'number' || !Number.isFinite(num)) return null;
    const complement = target - num;
    if (seen.has(complement)) return [seen.get(complement), i];
    if (!seen.has(num)) seen.set(num, i);
  }
  return null;
}

/**
 * Problem 3: count leads by status. Builds a new object; input is never modified.
 * Works for any status value. A Map is used while counting so odd status
 * strings such as "__proto__" cannot cause trouble.
 *
 * A status such as "toString" or "valueOf" collides with the inherited
 * Object.prototype methods. Writing such a count as a normal property would
 * replace the method with a number and make the result throw
 * "Cannot convert object to primitive value" whenever it is coerced to a
 * string. Those counts are therefore not written directly onto the result;
 * the full set of counts is always available on result.counts under their
 * exact names. Normal statuses are still exposed directly on the result.
 */
function countLeadsByStatus(leads) {
  const counts = new Map();
  for (const lead of leads || []) {
    if (lead === null || typeof lead !== 'object') continue;
    const status = lead.status;
    // A lead without a status is counted under a stable, explicit key rather
    // than the string "undefined".
    const key = status === undefined || status === null ? 'UNKNOWN' : String(status);
    counts.set(key, (counts.get(key) || 0) + 1);
  }

  const result = { counts: Object.create(null) };
  for (const [key, value] of counts) {
    // Only safe names are written directly onto the result. The full set of
    // counts is always available on result.counts under their exact names.
    const isSafe = !['toString', 'valueOf', 'hasOwnProperty', 'constructor',
      '__proto__', '__defineGetter__', '__defineSetter__', 'isPrototypeOf',
      'propertyIsEnumerable', 'toLocaleString'].includes(key);
    if (isSafe) {
      result[key] = value;
    }
    result.counts[key] = value;
  }
  return result;
}

module.exports = { firstNonRepeatingChar, findTwoSum, countLeadsByStatus };
