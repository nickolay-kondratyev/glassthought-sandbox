/**
 * Hello world function
 * @param name The name to greet
 * @returns Greeting message
 */
function greet(name: string): string {
  return `Hello, ${name}!`;
}

console.log(greet('World'));

export { greet }; 