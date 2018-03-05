package src.test.taurus

class BasicSimulation extends Simulation {

    // parse load profile from Taurus
    val t_iterations = Integer.getInteger("iterations", 20).toInt
    val t_concurrency = Integer.getInteger("concurrency", 5).toInt
    val t_rampUp = Integer.getInteger("ramp-up", 1).toInt
    val t_holdFor = Integer.getInteger("hold-for", 60).toInt
    val t_throughput = Integer.getInteger("throughput", 100).toInt
    val t_defaultAddress = System.getProperty("default-address", "https://digitalservicesx.evobanco.com")
    val httpConf = http.baseURL(t_defaultAddress)




    // 'forever' means each thread will execute scenario until
    // duration limit is reached
    val loopScenario = scenario("Loop Scenario").forever() {
        exec(
            http("health")
                .get("/health")
        )
    }

    val execution =
        loopScenario
            .inject(rampUsers(t_concurrency) over t_rampUp)
            .protocols(httpConf)

    setUp(execution).
        throttle(jumpToRps(t_throughput), holdFor(t_holdFor)).
        maxDuration(t_rampUp + t_holdFor)
}