package src.test.taurus

import java.security.MessageDigest

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.util.Random

class BasicSimulation extends Simulation {
  val r = new scala.util.Random(31)
  
  def satisfactionGenerator(session: Session): String ={

    val channel = r.alphanumeric take 1 mkString("")
    val clientCode = (r.nextLong() % 1000000L).abs.toString.reverse.padTo(10, "0").mkString
    val communicationId = (r.alphanumeric take 12 mkString("")).reverse.padTo(12, "0").mkString
    val satisfaction = r.nextInt(2)

    val md = MessageDigest.getInstance("MD5")
    val text = s"clavedes$clientCode$communicationId"
    md.update(text.getBytes)
    val ky = md.digest.map("%02X" format _).mkString.toLowerCase
    
    val res = s"""{ "channel": "$channel", "clientCode": "$clientCode", "communicationId": "$communicationId", "satisfaction": "$satisfaction", "ky": "$ky" }"""


    res
  }
  
  // parse load profile from Taurus
  val t_iterations = Integer.getInteger("iterations", 20).toInt
  val t_concurrency = Integer.getInteger("concurrency", 5).toInt
  val t_rampUp = Integer.getInteger("ramp-up", 1).toInt
  val t_holdFor = Integer.getInteger("hold-for", 60).toInt
  val t_throughput = Integer.getInteger("throughput", 100).toInt
  //val t_defaultAddress = System.getProperty("default-address", "http://evo-public-api.evo-public-api-dev.svc:8080")
  val t_defaultAddress = System.getProperty("default-address", "https://digitalservicesx.evobanco.com")
  val httpConf = http.baseURL(t_defaultAddress)




  // 'forever' means each thread will execute scenario until
  // duration limit is reached
  val loopScenario = scenario("Loop Scenario").forever() {
    exec(
      http("satisfaction")
        //.post("/satisfaction")
        .get("/health")
        //.header("Content-Type", "application/json")
        //.body(StringBody(session => satisfactionGenerator(session))).asJSON
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