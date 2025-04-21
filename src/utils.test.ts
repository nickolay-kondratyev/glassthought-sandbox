import { add, subtract, isPalindrome } from './utils';

describe('Math functions', () => {
  // Basic test
  test('adds two numbers correctly', () => {
    expect(add(1, 2)).toBe(3);
    expect(add(-1, 1)).toBe(0);
    expect(add(5, 5)).toBe(10);
  });

  // Using test.each for data-driven tests
  test.each([
    [1, 1, 0],
    [5, 3, 2],
    [10, 5, 5],
    [0, 5, -5],
  ])('subtract(%i, %i) should return %i', (a, b, expected) => {
    expect(subtract(a, b)).toBe(expected);
  });
});

describe('String functions', () => {
  // Testing using toBeTruthy and toBeFalsy
  test('checks palindromes correctly', () => {
    expect(isPalindrome('racecar')).toBeTruthy();
    expect(isPalindrome('hello')).toBeFalsy();
  });

  // Using test.each with table format
  test.each`
    input                | expected
    ${'A man a plan a canal Panama'} | ${true}
    ${'No lemon, no melon'}  | ${true}
    ${'hello world'}         | ${false}
    ${'12321'}               | ${true}
    ${'not a palindrome'}    | ${false}
  `('isPalindrome("$input") should return $expected', ({ input, expected }) => {
    expect(isPalindrome(input)).toBe(expected);
  });
}); 