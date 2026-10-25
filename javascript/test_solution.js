'use strict';

const assert = require('assert');
const {
  firstNonRepeatingChar,
  findTwoSum,
  countLeadsByStatus,
} = require('./solution.js');

let pass = 0;
let fail = 0;

function check(name, fn) {
  try {
    fn();
    pass++;
    console.log('PASS  ' + name);
  } catch (e) {
    fail++;
    console.log('FAIL  ' + name + ' -> ' + e.message);
  }
}

// 1. First non-repeating character, counted case-insensitively.
check('firstNonRepeatingChar finds the unique character', () => {
  assert.strictEqual(firstNonRepeatingChar('swiss'), 'w');
  assert.strictEqual(firstNonRepeatingChar('aAbBcC'), null);
});

// 2. Two sum returns the pair of indices that add up to the target.
check('findTwoSum returns the matching indices', () => {
  assert.deepStrictEqual(findTwoSum([2, 7, 11, 15], 9), [0, 1]);
  assert.deepStrictEqual(findTwoSum([3, 2, 4], 6), [1, 2]);
});

// 3. No matching pair returns null.
check('findTwoSum returns null when there is no pair', () => {
  assert.strictEqual(findTwoSum([1, 2], 100), null);
});

// 4. Leads are grouped by status without modifying the input.
check('countLeadsByStatus counts each status', () => {
  const leads = [{ status: 'NEW' }, { status: 'NEW' }, { status: 'OLD' }];
  const counts = countLeadsByStatus(leads);
  assert.strictEqual(counts.NEW, 2);
  assert.strictEqual(counts.OLD, 1);
  assert.strictEqual(leads.length, 3);
});

// 5. A status that collides with an Object.prototype method stays retrievable
//    and the result can still be coerced to a string.
check('countLeadsByStatus handles a "toString" status', () => {
  const counts = countLeadsByStatus([{ status: 'toString' }]);
  assert.strictEqual(counts.counts.toString, 1);
  assert.strictEqual(String(counts), '[object Object]');
});

console.log('\n=== PASS: ' + pass + '  FAIL: ' + fail + ' ===');
if (fail > 0) {
  process.exit(1);
}
