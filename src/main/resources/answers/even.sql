-- Question 2 SQL Solution: Count younger employees by department
-- Problem: Calculate the number of employees who are younger than each employee, grouped by their respective departments

SELECT 
    e.EMP_ID,
    e.FIRST_NAME,
    e.LAST_NAME,
    d.DEPARTMENT_NAME,
    COUNT(y.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT
FROM EMPLOYEE e
JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
LEFT JOIN EMPLOYEE y ON e.DEPARTMENT = y.DEPARTMENT 
    AND y.DOB > e.DOB  -- y.DOB > e.DOB means y is younger than e
GROUP BY e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, d.DEPARTMENT_NAME
ORDER BY e.EMP_ID DESC;
