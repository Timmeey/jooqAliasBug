CREATE TABLE  account (
    foo TEXT
);

CREATE OR REPLACE FUNCTION bar_procedure(someText TEXT)
 RETURNS TABLE(id TEXT)
 LANGUAGE plpgsql
AS $function$
BEGIN
	RETURN QUERY
	SELECT someText;

END; $function$;