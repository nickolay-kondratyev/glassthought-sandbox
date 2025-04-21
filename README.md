# TypeScript Project

A TypeScript-ready project sandbox.

## Setup

```bash
# Install dependencies
npm install

# Run in development mode
npm run dev

# Build for production
npm run build

# Run the built version
npm start
```

## Testing

This project uses Jest for testing. Tests are written in TypeScript and can be found in files with `.test.ts` or `.spec.ts` extensions.

```bash
# Run tests
npm test

# Run tests in watch mode
npm run test:watch
```

### Writing Tests

Tests are written using the Jest framework. Here's a simple example:

```typescript
import { add } from './utils';

describe('Math functions', () => {
  test('adds two numbers correctly', () => {
    expect(add(1, 2)).toBe(3);
  });
});
```

Jest provides many matchers like `toBe()`, `toEqual()`, `toBeTruthy()`, and more for assertions.

For data-driven tests, you can use `test.each()`:

```typescript
test.each([
  [1, 1, 0],
  [5, 3, 2],
])('subtract(%i, %i) should return %i', (a, b, expected) => {
  expect(subtract(a, b)).toBe(expected);
});
```

## Project Structure

- `src/` - TypeScript source code
- `dist/` - Compiled JavaScript output
- `src/**/*.test.ts` - Jest tests
