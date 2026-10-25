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
  const seen = new Map();
  for (let i = 0; i < nums.length; i++) {
    const complement = target - nums[i];
    if (seen.has(complement)) return [seen.get(complement), i];
    if (!seen.has(nums[i])) seen.set(nums[i], i);
  }
  return null;
}

/**
 * Problem 3: count leads by status. Builds a new object; input is never modified.
 * Works for any status value. A Map is used while counting so odd status
 * strings such as "__proto__" cannot cause trouble.
 */
function countLeadsByStatus(leads) {
  const counts = new Map();
  for (const lead of leads || []) {
    counts.set(lead.status, (counts.get(lead.status) || 0) + 1);
  }
  return Object.fromEntries(counts);
}

module.exports = { firstNonRepeatingChar, findTwoSum, countLeadsByStatus };
