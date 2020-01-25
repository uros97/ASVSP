#!/usr/bin/python
### before spark-submit: export PYTHONIOENCODING=utf8

from pyspark import SparkContext, SparkConf
from pyspark.sql import SparkSession
from pyspark.sql.types import *

def quiet_logs(sc):
  logger = sc._jvm.org.apache.log4j
  logger.LogManager.getLogger("org"). setLevel(logger.Level.ERROR)
  logger.LogManager.getLogger("akka").setLevel(logger.Level.ERROR)

conf = SparkConf().setAppName("uni").setMaster("local")
sc = SparkContext(conf=conf)
spark = SparkSession(sc)

quiet_logs(spark)

from pyspark.sql.types import *

schemaString = "Year Quarter Month DayofMonth DayOfWeek FlightDate Reporting_Airline \
                DOT_ID_Reporting_Airline IATA_CODE_Reporting_Airline Tail_Number Flight_Number_Reporting_Airline \
                OriginAirportID OriginAirportSeqID OriginCityMarketID Origin OriginCityName OriginState \
                OriginStateFips OriginStateName OriginWac DestAirportID DestAirportSeqID DestCityMarketID \
                Dest DestCityName DestState DestStateFips DestStateName DestWac CRSDepTime DepTime DepDelay \
                DepDelayMinutes DepDel15 DepartureDelayGroups DepTimeBlk TaxiOut WheelsOff WheelsOn TaxiIn \
                CRSArrTime ArrTime ArrDelay ArrDelayMinutes ArrDel15 ArrivalDelayGroups ArrTimeBlk Cancelled \
                CancellationCode Diverted CRSElapsedTime ActualElapsedTime AirTime Flights Distance \
                DistanceGroup CarrierDelay WeatherDelay NASDelay SecurityDelay LateAircraftDelay \
                FirstDepTime TotalAddGTime LongestAddGTime DivAirportLandings DivReachedDest \
                DivActualElapsedTime DivArrDelay DivDistance Div1Airport Div1AirportID \
                Div1AirportSeqID Div1WheelsOn Div1TotalGTime Div1LongestGTime Div1WheelsOff Div1TailNum \
                Div2Airport Div2AirportID Div2AirportSeqID Div2WheelsOn Div2TotalGTime Div2LongestGTime \
                Div2WheelsOff Div2TailNum Div3Airport Div3AirportID Div3AirportSeqID Div3WheelsOn Div3TotalGTime \
                Div3LongestGTime Div3WheelsOff Div3TailNum Div4Airport Div4AirportID Div4AirportSeqID \
                Div4WheelsOn Div4TotalGTime Div4LongestGTime Div4WheelsOff Div4TailNum Div5Airport \
                Div5AirportID Div5AirportSeqID Div5WheelsOn Div5TotalGTime Div5LongestGTime Div5WheelsOff \
                Div5TailNum"
fields = [StructField(field_name, StringType(), True) for field_name in schemaString.split()]
schema = StructType(fields)

df = spark.read.csv("hdfs://namenode:9000/test/test2.csv", header=True, mode="DROPMALFORMED", schema=schema)

# df.printSchema()

df = df.withColumn("Quarter", df["Quarter"].cast(IntegerType()))
df = df.withColumn("Month", df["Month"].cast(IntegerType()))
df.createOrReplaceTempView("flights")
#------------------------------------------------- PLANE SCHEMA ------------------------------------------------------------------
planeSchemaString = "Tail_Number Model Manufacturer Engine_Type Aircraft_Type Production_Year Issue_Date Status Ownership"
planeFields = [StructField(field_name, StringType(), True) for field_name in planeSchemaString.split()]
planeSchema = StructType(planeFields)
dfPlane = spark.read.csv("hdfs://namenode:9000/test/plane-data.csv", header=True, mode="DROPMALFORMED", schema=planeSchema)
dfPlane.createOrReplaceTempView("planes")
dfPlane = dfPlane.withColumn("Production_Year", dfPlane["Production_Year"].cast(IntegerType()))
#--------------------------------------------------- Queries ----------------------------------------------------------------------

queryForDelayType = "SELECT Year, Month, count(*) NumberOfDelFlights, round(sum(ArrDelayMinutes)/count(*),1) AvgDelayInMin, \
         round(sum(CarrierDelay)/count(*),1) AvgCarrierDelayInMin, \
         round(sum(WeatherDelay)/count(*),1) AvgWeatherDelayInMin, \
         round(sum(NASDelay)/count(*),1) AvgNASDelayInMinutes, \
         round(sum(SecurityDelay)/count(*),1) AvgSecurityDelayInMin, \
         round(sum(LateAircraftDelay)/count(*),1) AvgLateAircraftDelayInMin \
         FROM flights \
         WHERE ArrDel15 = '1.00' \
         GROUP BY Year, Month \
         ORDER BY Year, Month"

queryForMonthlyDelay = "SELECT Year, Month, count(*) NumberOfFlights, \
                      round(sum(ArrDel15)*100/count(*),1) DelayedMoreThan15MinPercentage, \
                      round(sum(Cancelled)*100/count(*),1) CancelledPercentage, \
                      round(count(Div1Airport)*100/count(*),1) DivertedPercentage \
                      FROM flights \
                      GROUP BY Year, Month \
                      ORDER BY Year, Month"

queryForHourlyDelay = "SELECT DepTimeBlk HoursInterval, count(*) NumberOfFlights, \
                      round(sum(ArrDel15)*100/count(*),1) DelayedMoreThan15MinPercentage, \
                      round(sum(Cancelled)*100/count(*),1) CancelledPercentage, \
                      round(count(Div1Airport)*100/count(*),1) DivertedPercentage \
                      FROM flights \
                      GROUP BY DepTimeBlk \
                      ORDER BY DepTimeBlk"

queryForOriginDelay = "SELECT OriginAirportId AirportId, OriginStateName StateName, OriginCityName CityName, Origin AirportName, count(*) NumberOfFlights, \
                      round(sum(ArrDel15)*100/count(*),1) DelayedMoreThan15MinPercentage, \
                      round(sum(Cancelled)*100/count(*),1) CancelledPercentage, \
                      round(count(Div1Airport)*100/count(*),1) DivertedPercentage \
                      FROM flights \
                      GROUP BY OriginAirportId, OriginCityName, OriginStateName, Origin \
                      ORDER BY OriginStateName, OriginCityName"


queryDistinct = "SELECT count(DISTINCT OriginAirportId) FROM flights"

queryForPlanes = "SELECT Manufacturer Manufacturer, Model Model, FLOOR(avg(p.Production_Year)) AverageProdYear, count(*) NumberOfFlights, \
                  round(sum(ArrDel15)*100/count(*),1) DelayedMoreThan15MinPercentage, \
                  round(sum(Cancelled)*100/count(*),1) CancelledPercentage, \
                  round(count(Div1Airport)*100/count(*),1) DivertedPercentage \
                  FROM flights f, planes p \
                  WHERE f.Tail_Number = p.Tail_Number \
                  GROUP BY Manufacturer, Model \
                  ORDER BY Manufacturer, Model"
sqlPercentage = spark.sql(queryForMonthlyDelay)
sqlPercentage.show(20,False)
#sqlPercentage.coalesce(1).write.csv("hdfs://namenode:9000/results/monthlyDelay.csv", header = 'true')
#print("Monthly over...")

sqlDF = spark.sql(queryForDelayType)
sqlDF.show(20, False)
#sqlDF.coalesce(1).write.csv("hdfs://namenode:9000/results/delayType.csv", header = 'true')
#print("Delay type over...")

sqlHourly = spark.sql(queryForHourlyDelay)
sqlHourly.show(24,False)
#sqlHourly.coalesce(1).write.csv("hdfs://namenode:9000/results/hourlyDelay.csv", header = 'true')
#print("Hourly over...")

sqlOrigin = spark.sql(queryForOriginDelay)
sqlOrigin.show(6000,20, False)
#sqlOrigin.coalesce(1).write.csv("hdfs://namenode:9000/results/originDelay.csv", header = 'true')
#print("Origin over...")

#sqlDistinct = spark.sql(queryDistinct)
#sqlDistinct.show()
sqlPlanes = spark.sql(queryForPlanes)
sqlPlanes.show(1000, False)
#sqlPlanes.coalesce(1).write.csv("hdfs://namenode:9000/results/planesDelay.csv", header = 'true')
#print("Plane over...")


