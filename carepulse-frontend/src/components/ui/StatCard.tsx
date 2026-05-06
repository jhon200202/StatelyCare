import React from 'react';

interface StatCardProps {
  title: string;
  value: string | number;
  icon?: React.ReactNode;
  trend?: {
    value: number;
    isPositive: boolean;
  };
}

export const StatCard: React.FC<StatCardProps> = ({ title, value, icon, trend }) => {
  return (
    <div className="bg-surface-containerLowest p-6 rounded-lg border border-outline-variant flex flex-col">
      <div className="flex justify-between items-start mb-4">
        <h3 className="text-sm font-medium text-on-surfaceVariant uppercase tracking-wider">{title}</h3>
        {icon && <div className="text-primary-dim">{icon}</div>}
      </div>
      <div className="flex items-end gap-3 mt-auto">
        <span className="text-3xl font-headline font-bold text-on-surface">{value}</span>
        {trend && (
          <span className={`text-sm font-medium mb-1 ${trend.isPositive ? 'text-secondary' : 'text-error'}`}>
            {trend.isPositive ? '+' : '-'}{Math.abs(trend.value)}%
          </span>
        )}
      </div>
    </div>
  );
};
