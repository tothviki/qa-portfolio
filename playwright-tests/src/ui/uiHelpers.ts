import { AutomationInTestingTestData } from '../utils/automationInTestingTestData';

export function futureDate(offsetDays: number) {
  return AutomationInTestingTestData.isoDateFromToday(offsetDays);
}

export function randomName() {
  const id = AutomationInTestingTestData.nextId('ui-name');
  return `Test ${id}`;
}

export function randomEmail() {
  return `${AutomationInTestingTestData.nextId('ui-email')}@example.com`;
}
