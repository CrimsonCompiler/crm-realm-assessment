'use strict';

const assert = require('assert');
const {
  firstNonRepeatingChar,
  findTwoSum,
  countLeadsByStatus,
} = require('./solution.js');

let pass = 0;
let fail = 0;

function check(label, fn) {
  try {
    fn();
    pass++;
    console.log('PASS  ' + label);
  } catch (e) {
    fail++;
    console.log('FAIL  ' + label + ' -> ' + e.message);
  }
}

console.log('--- firstNonRepeatingChar ---');
check('swiss -> w', () =>
  assert.strictEqual(firstNonRepeatingChar('swiss'), 'w'));
check('aAbBcC -> null (case-insensitive)', () =>
  assert.strictEqual(firstNonRepeatingChar('aAbBcC'), null));
check('aabb -> null', () =>
  assert.strictEqual(firstNonRepeatingChar('aabb'), null));
check('empty -> null', () =>
  assert.strictEqual(firstNonRepeatingChar(''), null));
check('single char -> itself', () =>
  assert.strictEqual(firstNonRepeatingChar('x'), 'x'));
check('non-string -> null', () =>
  assert.strictEqual(firstNonRepeatingChar(123), null));
check('null -> null', () =>
  assert.strictEqual(firstNonRepeatingChar(null), null));
check('original case preserved', () =>
  assert.strictEqual(firstNonRepeatingChar('Ab a'), 'b'));
check('emoji (outside BMP) handled', () =>
  assert.strictEqual(firstNonRepeatingChar('a🙂b🙂c'), 'a'));

console.log('\n--- findTwoSum ---');
check('[2,7,11,15],9 -> [0,1]', () =>
  assert.deepStrictEqual(findTwoSum([2, 7, 11, 15], 9), [0, 1]));
check('[3,2,4],6 -> [1,2]', () =>
  assert.deepStrictEqual(findTwoSum([3, 2, 4], 6), [1, 2]));
check('[3,3],6 -> [0,1]', () =>
  assert.deepStrictEqual(findTwoSum([3, 3], 6), [0, 1]));
check('no solution -> null', () =>
  assert.strictEqual(findTwoSum([1, 2], 100), null));
check('empty -> null', () =>
  assert.strictEqual(findTwoSum([], 5), null));
check('non-array -> null', () =>
  assert.strictEqual(findTwoSum('nope', 5), null));
check('string elements rejected', () =>
  assert.strictEqual(findTwoSum(['2', '7'], 9), null));
check('null element rejected', () =>
  assert.strictEqual(findTwoSum([2, null, 7], 9), null));
check('NaN target rejected', () =>
  assert.strictEqual(findTwoSum([1, 2], NaN), null));

console.log('\n--- countLeadsByStatus ---');
check('counts by status', () =>
  assert.deepStrictEqual(
    Object.assign({}, countLeadsByStatus([
      { status: 'NEW' }, { status: 'NEW' }, { status: 'OLD' },
    ])),
    { NEW: 2, OLD: 1 }));
check('empty -> empty', () =>
  assert.strictEqual(Object.keys(countLeadsByStatus([])).length, 0));
check('null input -> empty', () =>
  assert.strictEqual(Object.keys(countLeadsByStatus(null)).length, 0));
check('input not mutated', () => {
  const leads = [{ status: 'A' }];
  countLeadsByStatus(leads);
  assert.strictEqual(leads.length, 1);
});
check('missing status -> UNKNOWN key', () =>
  assert.strictEqual(countLeadsByStatus([{}, { status: undefined }]).UNKNOWN, 2));
check('null status -> UNKNOWN key', () =>
  assert.strictEqual(countLeadsByStatus([{ status: null }]).UNKNOWN, 1));
check('null lead entries skipped', () =>
  assert.strictEqual(Object.keys(countLeadsByStatus([null, undefined, { status: 'A' }])).length, 1));
check('"toString" status does not break coercion', () => {
  const r = countLeadsByStatus([{ status: 'toString' }]);
  // The count is stored under its own property, but the inherited method is
  // left intact so the object can still be coerced to a string.
  assert.strictEqual(r.toString, 1);
  assert.strictEqual(Object.prototype.hasOwnProperty.call(r, 'toString'), true);
});
check('"valueOf" status does not break coercion', () => {
  const r = countLeadsByStatus([{ status: 'valueOf' }]);
  assert.strictEqual(String(r), '[object Object]');
});
check('object with no shadowing coerces normally', () => {
  const r = countLeadsByStatus([{ status: 'NEW' }]);
  assert.strictEqual(String(r), '[object Object]');
});
check('"__proto__" is an own property, not pollution', () => {
  const r = countLeadsByStatus([{ status: '__proto__' }]);
  assert.strictEqual(Object.prototype.hasOwnProperty.call(r, '__proto__'), true);
  assert.strictEqual(r['__proto__'], 1);
  assert.strictEqual(({}).polluted, undefined);
});
check('inherited toString still available when not shadowed', () => {
  const r = countLeadsByStatus([{ status: 'X' }]);
  assert.strictEqual(typeof r.toString, 'function');
});
check('numeric status coerced to string key', () =>
  assert.strictEqual(countLeadsByStatus([{ status: 42 }])['42'], 1));

console.log('\n=== PASS: ' + pass + '  FAIL: ' + fail + ' ===');
if (fail > 0) process.exit(1);
