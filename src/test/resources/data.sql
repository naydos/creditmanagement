
INSERT INTO CUSTOMER ( name, surname, credit_limit, used_credit_limit)
VALUES ('Ali', 'Veli', 10000, 0);


INSERT INTO LOAN ( loan_amount, number_of_installment, create_date, is_paid, customer_id)
VALUES (6000.00, 3, CURRENT_DATE, false, 1);


INSERT INTO LOAN_INSTALLMENT (loan_id, amount, paid_amount, due_date, payment_date, is_paid)
SELECT id, 2000.00, 0.00, DATEADD('MONTH', 1, CURRENT_DATE), NULL, false FROM LOAN WHERE loan_amount = 6000.00 ORDER BY id DESC LIMIT 1;
INSERT INTO LOAN_INSTALLMENT (loan_id, amount, paid_amount, due_date, payment_date, is_paid)
SELECT id, 2000.00, 0.00, DATEADD('MONTH', 2, CURRENT_DATE), NULL, false FROM LOAN WHERE loan_amount = 6000.00 ORDER BY id DESC LIMIT 1;
INSERT INTO LOAN_INSTALLMENT (loan_id, amount, paid_amount, due_date, payment_date, is_paid)
SELECT id, 2000.00, 0.00, DATEADD('MONTH', 3, CURRENT_DATE), NULL, false FROM LOAN WHERE loan_amount = 6000.00 ORDER BY id DESC LIMIT 1;

