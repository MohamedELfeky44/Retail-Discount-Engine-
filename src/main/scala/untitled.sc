import java.time.LocalDate
import scala.io.Source
import scala.math.ceil

"2024-05-02T12:07:22".split("T")(0)
ceil(20/60)

30.0/60

val lines = List("2023-04-18T18:18:40Z,Wine - White Pinot Grigio","2023-06-10,6,122.47,Store,Visa")

case class Order(timestamp: String,
                 product_name: String,
                 expiry_date: LocalDate,
                 quantity: Int,
                 unit_price: Double,
                 channel:String,
                 payment_method: String)

def toOrder(line: String): Order ={
  val Array(timestamp,product_name,expiry_date,quantity,unit_price,channel,payment_method) = line.split(",")
  Order(timestamp,product_name,LocalDate.parse(expiry_date),quantity.toInt,unit_price.toDouble,channel,payment_method)
}

lines.map(toOrder)

