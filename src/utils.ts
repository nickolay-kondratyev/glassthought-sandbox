/**
 * Adds two numbers together
 */
export function add(a: number, b: number): number {
  return a + b;
}

/**
 * Subtracts second number from the first
 */
export function subtract(a: number, b: number): number {
  return a - b;
}

/**
 * Checks if a string is a palindrome
 */
export function isPalindrome(str: string): boolean {
  const normalized = str.toLowerCase().replace(/[^a-z0-9]/g, '');
  const reversed = normalized.split('').reverse().join('');
  return normalized === reversed;
} 