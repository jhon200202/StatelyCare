import React from 'react';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Legend } from 'recharts';
import type { FinancialTrend } from '../../modules/dashboard/domain/types/DashboardTypes';
import { designTokens } from '../../styles/designTokens';

interface FinancialChartProps {
  data: FinancialTrend[];
}

export const FinancialChart: React.FC<FinancialChartProps> = ({ data }) => {
  return (
    <div className="h-80 w-full mt-4">
      <ResponsiveContainer width="100%" height="100%">
        <AreaChart data={data} margin={{ top: 10, right: 30, left: 0, bottom: 0 }}>
          <defs>
            <linearGradient id="colorRevenue" x1="0" y1="0" x2="0" y2="1">
              <stop offset="5%" stopColor={designTokens.colors.primary} stopOpacity={0.8}/>
              <stop offset="95%" stopColor={designTokens.colors.primary} stopOpacity={0}/>
            </linearGradient>
            <linearGradient id="colorExpenses" x1="0" y1="0" x2="0" y2="1">
              <stop offset="5%" stopColor={designTokens.colors.error} stopOpacity={0.8}/>
              <stop offset="95%" stopColor={designTokens.colors.error} stopOpacity={0}/>
            </linearGradient>
          </defs>
          <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#e5eeff" />
          <XAxis dataKey="month" axisLine={false} tickLine={false} tick={{ fill: '#72787f' }} dy={10} />
          <YAxis axisLine={false} tickLine={false} tick={{ fill: '#72787f' }} />
          <Tooltip 
            contentStyle={{ borderRadius: '8px', border: '1px solid #c1c7cf', boxShadow: '0 4px 6px -1px rgb(0 0 0 / 0.1)' }}
          />
          <Legend iconType="circle" wrapperStyle={{ paddingTop: '20px' }}/>
          <Area type="monotone" name="Ingresos" dataKey="revenue" stroke={designTokens.colors.primary} fillOpacity={1} fill="url(#colorRevenue)" />
          <Area type="monotone" name="Egresos" dataKey="expenses" stroke={designTokens.colors.error} fillOpacity={1} fill="url(#colorExpenses)" />
        </AreaChart>
      </ResponsiveContainer>
    </div>
  );
};
