import java.text.SimpleDateFormat
import java.util.Date

object test {
  def main(args: Array[String]): Unit = {
//    val sdf = new SimpleDateFormat("yyyy-MM-dd")
//      for(i <- 1 to 10){
//        println(sdf.format(randomDate("1970-01-01","2010-01-01")))
//      }
    }

  def randomDate(beginDate: String, endDate: String): Date = {
    try {
      val format = new SimpleDateFormat("yyyy-MM-dd")
      val start = format.parse(beginDate)
      val end = format.parse(endDate)
      if (start.getTime >= end.getTime) return null
      val date = random(start.getTime, end.getTime)
      return new Date(date)
    } catch {
      case e: Exception =>
        e.printStackTrace()
    }
    null
  }

  private def random(begin: Long, end: Long): Long = {
    val rtn = begin + (Math.random * (end - begin)).toLong
    if (rtn == begin || rtn == end) return random(begin, end)
    rtn
  }
}
