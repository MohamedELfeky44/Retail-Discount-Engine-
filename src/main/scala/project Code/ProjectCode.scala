package projectCode

import scala.io.Source
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.sql.{Connection, DriverManager, PreparedStatement}
import java.io.{File, FileOutputStream, PrintWriter}



/**
 * Provides a method to establish a connection to the database.
 *
 * This object encapsulates the details required to establish a database connection,
 * including the URL, username, and password. The getConnection method retrieves
 * a Connection object from the DriverManager using the specified URL, username,
 * and password.
 *
 * @return A Connection object representing the connection to the database.
 */
object DatabaseConnector {
    val url = "jdbc:oracle:thin:@//localhost:1521/XE"
    val username = "mohamed"
    val password = "root"

    def getConnection(): Connection = {
        DriverManager.getConnection(url, username, password)
    }
}


object QualifyCalculate extends App{
    // Load all lines from the file
    val lines = Source.fromFile("src/main/resources/TRX1000.csv").getLines().toList.tail

    /**
     * Initializes a file writer for logging order processing information.
     *
     * This section of code creates a File object representing the log file
     * where order processing information will be written. A PrintWriter is
     * then instantiated using a FileOutputStream with the file object and
     * a boolean value 'true' to enable appending to an existing file.
     *
     * @param f The File object representing the log file.
     * @param writer The PrintWriter object for writing to the log file.
     */


    /**
     * Represents an order with details such as timestamp, product name, expiry date, quantity,
     * unit price, channel, and payment method.
     *
     * This case class encapsulates the attributes of an order, providing a convenient way
     * to store and manipulate order data. Each order includes information such as the time
     * of purchase, the product name, the expiry date, the quantity purchased, the unit price,
     * the channel through which the order was made, and the payment method used.
     *
     * @param timestamp      The timestamp indicating when the order was placed.
     * @param product_name   The name of the product being ordered.
     * @param expiry_date    The expiry date of the product.
     * @param quantity       The quantity of the product being ordered.
     * @param unit_price     The unit price of the product.
     * @param channel        The channel through which the order was made (e.g., website, app).
     * @param payment_method The payment method used for the order (e.g., Visa).
     */
    case class Order(timestamp: String,
                     product_name: String,
                     expiry_date: LocalDate,
                     quantity: Int,
                     unit_price: Double,
                     channel:String,
                     payment_method: String)

    /**
     * Converts a string representation of an order into an Order object.
     *
     * This method parses a comma-separated string containing order details
     * such as timestamp, product name, expiry date, quantity, unit price,
     * channel, and payment method, and constructs an Order object.
     *
     * @param line The string representing an order with comma-separated values.
     * @return An Order object containing the parsed order details.
     */
    def toOrder(line: String): Order ={
        val Array(timestamp,product_name,expiry_date,quantity,unit_price,channel,payment_method) = line.split(",")
        Order(timestamp,product_name,LocalDate.parse(expiry_date),quantity.toInt,unit_price.toDouble,channel,payment_method)
    }

    /**
     * Extracts the date portion from a timestamp string.
     *
     * This method takes a timestamp string in the format "yyyy-MM-ddTHH:mm:ss" and
     * extracts the date portion (yyyy-MM-dd). It splits the timestamp string using "T"
     * as the delimiter and returns the first part, which represents the date.
     *
     * @param timestamp The timestamp string to extract the date from.
     * @return A string containing only the date portion of the timestamp.
     */
    def getData(timestamp: String): String ={
        timestamp.split("T")(0)
    }

    /**
     * List of discount rules and corresponding calculation functions.
     *
     * This list contains tuples where each tuple consists of two functions:
     *   1. A predicate function that determines if an order qualifies for a discount.
     *   2. A calculation function that computes the discount for the qualified order.
     *
     * The list includes various discount rules based on different criteria such as product type,
     * purchase date, expiry date, quantity, payment method, and channel. Each rule is associated
     * with a corresponding calculation function to determine the discount amount.
     *
     * @param order The Order object representing the order to be evaluated for discounts.
     * @return A tuple containing a boolean value indicating if the order qualifies for a discount,
     *         and a function to calculate the discount amount if the order qualifies.
     */
    val discountRules: List[(Order => Boolean, Order => Double)] = List(

        ((order: Order) => cheeseAndWineQualify(order), (order: Order) => cheeseAndWine(order)),

        ((order: Order) => javaDateBQualified(order), (order: Order) => javaDate(order)),

        ((order: Order) => expireDateQualify(order), (order: Order) => expireDate(order)),

        ((order: Order) => productQuantityQualify(order), (order: Order) => productQuantity(order)),

        ((order: Order) => productpayment_methodQualify(order), (order: Order) => productpayment_method(order)),

        ((order: Order) => productChannelQualify(order), (order: Order) => productChannel(order))

    )

    /**
     * Discount qualification criteria for orders.
     *
     * This section defines several methods that determine if an order qualifies for a discount based on various criteria,
     * including product type, order date, proximity of the expiry date, product quantity, payment method, and channel.
     * Each method evaluates specific attributes of the order and returns true if the order meets the qualification
     * criteria for a discount, otherwise it returns false.
     *
     * - cheeseAndWineQualify: Checks if the product name contains keywords "wine" or "cheese".
     * - javaDateBQualified: Checks if the order date is March 23rd.
     * - expireDateQualify: Checks if the proximity of the expiry date is less than 30 days.
     * - productQuantityQualify: Checks if the product quantity is greater than 5.
     * - productpayment_methodQualify: Checks if the payment method is "Visa".
     * - productChannelQualify: Checks if the channel through which the order was made is "App".
     *
     * @param order The Order object representing the order to be evaluated for discount qualification.
     * @return True if the order meets the qualification criteria for a discount, false otherwise.
     */
    def cheeseAndWineQualify(order:Order): Boolean ={
        if (order.product_name.toLowerCase.contains("wine") ||
                order.product_name.toLowerCase.contains("cheese")) {true} else {false}
    }

    def javaDateBQualified(order:Order): Boolean ={
        val orderDate = LocalDate.parse(getData(order.timestamp))
        if (orderDate.getMonthValue == 3 && orderDate.getDayOfMonth == 23) {true}
        else {false}
    }

    def expireDateQualify(order:Order): Boolean ={
        val orderDate = LocalDate.parse(getData(order.timestamp))
        ChronoUnit.DAYS.between(orderDate, order.expiry_date) < 30
    }

    def productQuantityQualify(order:Order): Boolean ={
        if (order.quantity > 5) {true}
        else {false}
    }

    def productpayment_methodQualify(order:Order): Boolean ={
        if (order.payment_method == "Visa") {true}
        else {false}
    }

    def productChannelQualify(order:Order): Boolean ={
        if (order.channel == "App") {true}
        else {false}
    }

    /**
     * Discount calculation methods for orders.
     *
     * This section defines several methods that calculate the discount amount for orders based on different criteria,
     * including product type, order date, proximity of the expiry date, product quantity, payment method, and channel.
     * Each method takes an Order object as input and computes the discount amount based on the specified criteria.
     *
     * - cheeseAndWine: Calculates a discount of 0.1 if the product name contains "cheese", otherwise 0.05.
     * - javaDate: Calculates a discount of 0.5 for orders placed on March 23rd.
     * - expireDate: Calculates a discount based on the proximity of the expiry date, with a rate of 0.01 per day.
     * - productQuantity: Calculates a discount based on the quantity of the product purchased:
     *     - 0.1 for quantities >= 15, 0.07 for quantities >= 10, 0.05 for quantities >= 6, and 0.0 otherwise.
     * - productpayment_method: Calculates a flat discount rate of 0.05 for orders with payment method "Visa".
     * - productChannel: Calculates a discount based on the quantity of the product and channel used for purchase.
     *
     * @param order The Order object for which the discount amount is to be calculated.
     * @return The calculated discount amount as a Double value.
     */
    def cheeseAndWine(order:Order) : Double={if (order.product_name.toLowerCase.contains("cheese")) {0.1} else {0.05}}
    def javaDate(order:Order) : Double={0.5}
    def expireDate(order:Order) : Double={
        val orderDate = LocalDate.parse(getData(order.timestamp))
        (30 -ChronoUnit.DAYS.between(orderDate, order.expiry_date)) * 0.01
    }
    def productQuantity(order:Order): Double ={
        if (order.quantity >= 15) {0.1}
        else if (order.quantity >= 10) {0.07}
        else if (order.quantity >= 6)  {0.05}
        else {0.0}
    }
    def productpayment_method(order:Order) : Double={0.05}
    def productChannel(order:Order) : Double={

        (math.round(order.quantity / 5.0) * 5).toDouble * 0.01
    }

    /**
     * Calculates the total discount amount for an order based on the specified discount rules.
     *
     * This method takes an Order object and a list of discount rules, where each rule consists of two functions:
     *   1. A predicate function that determines if the rule applies to the order.
     *   2. A calculation function that computes the discount amount if the rule applies.
     *
     * The method evaluates each rule against the order and collects the discount amounts from the rules
     * that apply. It then selects the top two discount amounts and calculates their average as the total discount.
     *
     * @param order The Order object for which the discount amount is to be calculated.
     * @param rules The list of discount rules, each containing a predicate function and a calculation function.
     * @return The total discount amount for the order as a Double value.
     */
    def getOrdersWithDiscount(order:Order,  rules: List[(Order => Boolean, Order => Double)]): Double ={
        val qualifiedRules = rules.filter(rule => rule._1(order)) // return rules that apply on specific order
        val discounts = qualifiedRules.map(rule => rule._2(order)).sorted.reverse.take(2)
        discounts.sum / discounts.length
    }

    /**
     * Processes an order by calculating discounts and inserting it into the database.
     *
     * This method takes an Order object and a list of discount rules as input. It extracts various attributes
     * from the order such as timestamp, product name, expiry date, quantity, unit price, channel, and payment method.
     * It then calculates the discount for the order using the provided discount rules and inserts the order into
     * the database. Finally, it returns a string containing the timestamp, product name, and discount applied to the order.
     *
     * @param order        The Order object representing the order to be processed.
     * @param discountRules  The list of discount rules used to calculate discounts for the order.
     * @return A string containing the timestamp, product name, and discount applied to the order.
     */

    def processOrder(order: Order, discountRules: List[(Order => Boolean, Order => Double)]): Unit = {
        val f: File = new File("src/main/resources/orders_log.txt")
        val writer = new PrintWriter(new FileOutputStream(f,true))
        val timeStamp = order.timestamp
        val product_name = order.product_name
        val exprie_date = order.expiry_date
        val quantity = order.quantity
        val unit_price = order.unit_price
        val channel = order.channel
        val payment_method = order.payment_method
        writeLine(LocalDate.now().toString+","+"Connection Started",writer)
        val discount = if (getOrdersWithDiscount(order, discountRules).isNaN) {
            0
            }
            else {
                getOrdersWithDiscount(order, discountRules)
            }
        val final_price = (unit_price*quantity) - (discount * unit_price* quantity)
        insertOrder(timeStamp, product_name, exprie_date, quantity, unit_price, channel, payment_method, discount, final_price)
        writeLine(timeStamp+","+product_name+","+discount,writer)
        writeLine(LocalDate.now().toString+","+"Connection Closed",writer)
        writer.close()
    }

    /**
     * Inserts an order into the database.
     *
     * This method inserts an order into the database table named "mohamed.orders". It takes various parameters
     * representing the attributes of the order, such as timestamp, product name, expiry date, quantity, unit price,
     * channel, payment method, discount, and final price. It establishes a database connection, prepares an SQL
     * statement for insertion, sets the parameter values, executes the statement, and closes the resources.
     *
     * @param timeStamp      The timestamp indicating when the order was placed.
     * @param product_name   The name of the product being ordered.
     * @param exprie_date    The expiry date of the product.
     * @param quantity       The quantity of the product being ordered.
     * @param unit_price     The unit price of the product.
     * @param channel        The channel through which the order was made (e.g., website, app).
     * @param payment_method The payment method used for the order (e.g., Visa, Mastercard).
     * @param discount       The discount amount applied to the order.
     * @param final_price    The final price after applying the discount.
     */
    private def insertOrder(timeStamp: String, product_name: String, exprie_date: LocalDate, quantity: Int,
                            unit_price: Double, channel: String, payment_method: String, discount: Double,
                            final_price: Double): Unit =
    {
        val connection = DatabaseConnector.getConnection()
        val preparedStatement = connection.prepareStatement(
            "INSERT INTO mohamed.orders (timestamp, product_name, expiry_date, quantity, unit_price," +
              " channel, payment_method, discount, final_price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")

        preparedStatement.setString(1, timeStamp)
        preparedStatement.setString(2, product_name)
        preparedStatement.setDate(3, java.sql.Date.valueOf(exprie_date))
        preparedStatement.setInt(4, quantity)
        preparedStatement.setDouble(5, unit_price)
        preparedStatement.setString(6, channel)
        preparedStatement.setString(7, payment_method)
        preparedStatement.setDouble(8, discount)
        preparedStatement.setDouble(9, final_price)
        preparedStatement.executeUpdate()
        //println("------------------") debugging orders insertion in the database
        preparedStatement.close()
        connection.close()
    }

    /**
     * Writes a line of text to a file.
     *
     * This method writes a line of text to the file represented by the PrintWriter object 'writer'.
     * It appends a newline character to the provided line before writing it to the file.
     *
     * @param line The line of text to be written to the file.
     */
    def writeLine(line: String,writer: PrintWriter): Unit = {
        writer.write(line+"\n")
    }



    /**
     * Processes each line of input, calculates discounts, and writes results to a file.
     *
     * This block of code reads each line of input, converts it into an Order object using the `toOrder` method,
     * and then processes the order using the `processOrder` method with the provided `discountRules`. The result,
     * containing the timestamp, product name, and discount applied, is obtained and written to a file using the
     * `writeLine` method. This process is repeated for each line of input.
     */
    lines.map(toOrder).foreach { l =>processOrder(l, discountRules)}




}
