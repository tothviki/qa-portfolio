import { expect } from '@playwright/test';
import { ZodError, ZodType } from 'zod';

export function expectMatchesSchema<T>(value: unknown, schema: ZodType<T>): T {
  const result = schema.safeParse(value);
  if (!result.success) {
    const message = formatZodError(result.error);
    expect(message).toEqual('');
    throw new Error(message);
  }

  return result.data;
}

function formatZodError(error: ZodError): string {
  return error.issues
    .map((issue) => {
      const path = issue.path.length > 0 ? issue.path.join('.') : 'response';
      return `${path}: ${issue.message}`;
    })
    .join('\n');
}
