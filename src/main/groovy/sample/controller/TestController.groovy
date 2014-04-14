package sample.controller

import com.datastax.driver.core.ResultSet
import com.datastax.driver.core.Row
import com.datastax.driver.core.Session
import com.datastax.driver.core.Statement
import com.datastax.driver.core.querybuilder.Clause
import com.datastax.driver.core.querybuilder.QueryBuilder
import com.datastax.driver.core.querybuilder.Select
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class TestController {

    @Autowired
    Session session

    @RequestMapping("/")
    @ResponseBody
    String home() {

        Statement insert = QueryBuilder.update("alist")
                .with(QueryBuilder.append("visits", new Date()))
                .where(QueryBuilder.eq("page", "/"))
        session.execute(insert);

        Statement select = QueryBuilder.select()
                .from("person")
                .where(QueryBuilder.eq("lastname", "Beck"))

        ResultSet rs = session.execute(select)


        String out = ""
        rs.each { r ->
            out += r.getString("firstName") + " " + r.getString("lastName") + "\n"
        }

        return " ${rs.availableWithoutFetching} - Hello World! \n\n ${out}";
    }

    @RequestMapping("/visits")
    @ResponseBody
    String visits() {

        def rs = session.execute("SELECT * FROM alist WHERE page = '/'")

        Row row = rs.one()
        def visits = row.getList("visits", Date.class)
        return "Visits: ${visits.size()}"
    }
}
