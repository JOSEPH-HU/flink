package com.joseph

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

/**
  * Created by hc360 on 17-11-15.
  */
object SocketWindowWordCount {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val text: DataStream[String] = env.socketTextStream("192.168.19.181", 9001, '\n')
    val windowCounts = text
      .flatMap { w => w.split("\\s") }
      .map { w => WordWithCount(w, 1) }
      .keyBy("word")
      .timeWindow(Time.seconds(5))
      .sum("count")

    windowCounts.print().setParallelism(1)

    env.execute("Socket Window WordCount")
  }

  case class WordWithCount(word: String, count: Long)

}
