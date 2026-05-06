import React from 'react';

interface Column<T> {
  key: keyof T;
  header: string;
  render?: (value: any, row: T) => React.ReactNode;
}

interface DataTableProps<T> {
  data: T[];
  columns: Column<T>[];
  onRowDoubleClick?: (row: T) => void;
}

export function DataTable<T extends { id: string | number }>({ data, columns, onRowDoubleClick }: DataTableProps<T>) {
  return (
    <div className="w-full overflow-x-auto border border-outline-variant rounded-lg bg-surface-containerLowest">
      <table className="w-full text-left border-collapse">
        <thead>
          <tr className="bg-surface-container border-b border-outline-variant text-on-surfaceVariant text-label-caps uppercase tracking-wider">
            {columns.map((col) => (
              <th key={col.key.toString()} className="px-6 py-4 font-semibold">
                {col.header}
              </th>
            ))}
          </tr>
        </thead>
        <tbody className="text-body-sm text-on-surface">
          {data.map((row, index) => (
            <tr 
              key={row.id} 
              onDoubleClick={() => onRowDoubleClick?.(row)}
              className={`border-b border-outline-variant hover:bg-surface-containerLow transition-colors ${onRowDoubleClick ? 'cursor-pointer' : ''} ${index === data.length - 1 ? 'border-none' : ''}`}
            >
              {columns.map((col) => (
                <td key={col.key.toString()} className="px-6 py-4">
                  {col.render ? col.render(row[col.key], row) : String(row[col.key])}
                </td>
              ))}
            </tr>
          ))}
          {data.length === 0 && (
            <tr>
              <td colSpan={columns.length} className="px-6 py-8 text-center text-on-surfaceVariant">
                No hay datos disponibles
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}
