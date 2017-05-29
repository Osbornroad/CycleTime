SELECT quantity, startDateTime, duration,
employee_table.employeeName, process_table.processName, machine_table.machineName, parts_table.partsName
FROM sample_table
LEFT JOIN employee_table ON sample_table.employeeId = employee_table._id
LEFT JOIN process_table ON sample_table.processId = process_table._id
LEFT JOIN machine_table ON sample_table.machineId = machine_table._id
LEFT JOIN parts_table ON sample_table.partId = parts_table._id