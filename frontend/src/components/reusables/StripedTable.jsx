import React from "react";

const StripedTable = ({ columns, data, isLoading, error, emptyMessage }) => {
  if (isLoading) {
    return (
      <div className="text-center py-8">
        <div className="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500"></div>
        <p className="text-gray-400 mt-4">Loading...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="text-center py-8">
        <p className="text-red-400">{error}</p>
      </div>
    );
  }

  const [maxRows, setMaxRows] = React.useState(10);
  const tableRef = React.useRef(null);

  React.useEffect(() => {
    function updateMaxRows() {
      if (!tableRef.current) return;
      const headerHeight = 56;
      const rowHeight = 56;
      const availableHeight = window.innerHeight - tableRef.current.getBoundingClientRect().top - 32;
      const rows = Math.floor((availableHeight - headerHeight) / rowHeight);
      setMaxRows(rows > 0 ? rows : 1);
    }
    updateMaxRows();
    window.addEventListener('resize', updateMaxRows);
    return () => window.removeEventListener('resize', updateMaxRows);
  }, []);

  const visibleData = data.slice(0, maxRows);

  return (
    <div className="w-full rounded-lg border border-gray-800" ref={tableRef}>
      <table className="min-w-full divide-y-2 divide-gray-800 bg-gray-900 text-sm">
        <thead className="bg-gray-800">
          <tr>
            {columns.map((col) => (
              <th key={col.key} className="whitespace-nowrap px-4 py-3 text-left font-medium text-gray-100">
                {col.label}
              </th>
            ))}
          </tr>
        </thead>
        <tbody className="divide-y divide-gray-800">
          {visibleData.length === 0 ? (
            <tr>
              <td colSpan={columns.length} className="text-center py-12 text-gray-400">
                {emptyMessage || 'No data available'}
              </td>
            </tr>
          ) : (
            visibleData.map((row, index) => (
              <tr key={row.id || index} className="hover:bg-gray-800 transition-colors">
                {columns.map((col) => (
                  <td key={col.key} className="whitespace-nowrap px-4 py-3 text-gray-400">
                    {col.render ? col.render(row) : row[col.key]}
                  </td>
                ))}
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
};

export default StripedTable;