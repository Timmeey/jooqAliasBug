import de.timmeey.jooqAliasBug.jooq.Tables
import de.timmeey.jooqAliasBug.jooq.Tables.BAR_PROCEDURE
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.junit.jupiter.api.Test
import java.sql.DriverManager


class StoredProcTesrt() {
	var userName = "postgres"
	var password = "example"
	var url = "jdbc:postgresql://localhost:5431/bug"

	@Test
	fun doesNotWOrk() {
		val jooq = DSL.using(SQLDialect.POSTGRES)
		val AC = Tables.ACCOUNT.`as`("ac")
		val BAR = Tables.BAR_PROCEDURE.`as`("bar")

		val sql = jooq.insertInto(AC).columns(
				AC.FOO
		).select(jooq.select(
				BAR.ID)
				         .from(BAR.call("NotWorkingExample"))
		).toString()

		println(sql)
		// prints
		//insert into "public"."account" as "ac" ("foo")
		//select "bar"."id"
		//from "public"."bar"('NotWorkingExample')
	}

	@Test
	fun doesWork() {
		val jooq = DSL.using(SQLDialect.POSTGRES)
		val AC = Tables.ACCOUNT.`as`("ac")
		val BAR = Tables.BAR_PROCEDURE.`as`("bar")

		val sql = jooq.insertInto(AC).columns(
				AC.FOO
		).select(jooq.select(
				BAR.ID)
				         .from(BAR_PROCEDURE.call("SomeWorkingExample").`as`("bar"))
		).toString()

		println(sql)

		//prints
		//insert into "public"."account" as "ac" ("foo")
		//select "bar"."id"
		//from "public"."bar_procedure"('SomeWorkingExample') as "bar"
	}

	@Test
	fun integrationTestNotWorking() {
		// Connection is the only JDBC resource that we need
		// PreparedStatement and ResultSet are handled by jOOQ, internally
		DriverManager.getConnection(url, userName, password).use { conn ->
			val jooq = DSL.using(conn, SQLDialect.POSTGRES)
			val AC = Tables.ACCOUNT.`as`("ac")
			val BAR = Tables.BAR_PROCEDURE.`as`("bar")
			jooq.insertInto(AC).columns(
					AC.FOO
			).select(jooq.select(
					BAR.ID)
					         .from(BAR.call("NotWorkingExample"))
			).execute()
		}

	}


	@Test
	fun integrationTestDoesWork() {
		// Connection is the only JDBC resource that we need
		// PreparedStatement and ResultSet are handled by jOOQ, internally
		DriverManager.getConnection(url, userName, password).use { conn ->
			val jooq = DSL.using(conn, SQLDialect.POSTGRES)
			val AC = Tables.ACCOUNT.`as`("ac")
			val BAR = Tables.BAR_PROCEDURE.`as`("bar")
			jooq.insertInto(AC).columns(
					AC.FOO
			).select(jooq.select(
					BAR.ID)
					         .from(BAR_PROCEDURE.call("SomeWorkingExample").`as`("bar"))
			).execute()
		}

	}
}