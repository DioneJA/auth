import { Capacitor } from '@capacitor/core';
import { CapacitorSQLite, SQLiteConnection } from '@capacitor-community/sqlite';

const sqlite = new SQLiteConnection(CapacitorSQLite);
let db: any;

export async function initLocalDb() {
  if (!Capacitor.isNativePlatform()) {
    return;
  }
  db = await sqlite.createConnection('involvest', false, 'no-encryption', 1, false);
  await db.open();
  await db.execute(`
    CREATE TABLE IF NOT EXISTS cached_dashboard (
      id INTEGER PRIMARY KEY,
      payload TEXT NOT NULL,
      created_at TEXT NOT NULL
    );
  `);
}

export async function saveDashboardCache(payload: string) {
  if (!db) return;
  await db.run('INSERT INTO cached_dashboard (payload, created_at) VALUES (?, ?)', [payload, new Date().toISOString()]);
}

export async function getLastDashboardCache() {
  if (!db) return null;
  const res = await db.query('SELECT payload FROM cached_dashboard ORDER BY id DESC LIMIT 1');
  return res.values?.[0]?.payload ?? null;
}
