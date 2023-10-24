//package strings

//import WO1.doubleCase
//import WeatherData


import org.junit.jupiter.api.Assertions
//import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat


class ProcessingDataTest {

    //TO TESTS WITH FORBIDDEN VALUES


    @Test
    fun testFilteringWeatherData() {
        val testWeatherData1 = WeatherData("03.01.2020",	"BHM", 39.0,46.0,32.0,4.33)
        val testWeatherData3 = WeatherData("03.01.2020",	"BHM", 70.0,46.0,32.0,9.33)
        val testWeatherData4 = WeatherData("17.01.2020",	"DEF", 60.0,60.0,60.0,16.0)
        val testWeatherData2 = WeatherData("17.01.2020",	"ABC", 60.0,60.0,60.0,0.0)

        // Create a list of WeatherData objects
        val testWeatherDataList = listOf(testWeatherData1, testWeatherData2, testWeatherData3,testWeatherData4)

        // Filter the list using the filterWeatherDataList function
        val filteredWeatherDataList = filterWeatherDataList(testWeatherDataList)

        //Check if Size is still the Same
        Assertions.assertEquals(4, filteredWeatherDataList.size)

        //Check if Values which have been 60.0,60.0,60.0,0.0 are now 0.0,0.0,0.0,0.0
        val testWeatherData5 = WeatherData("17.01.2020",	"ABC", 0.0,0.0,0.0,0.0)
        //Assertions.assertEquals(filteredWeatherDataList[1], testWeatherData5)
        Assertions.assertEquals(filteredWeatherDataList[3], testWeatherData4)
        println(filteredWeatherDataList[1])
        Assertions.assertEquals(filteredWeatherDataList[1], testWeatherData5)
    }
    @Test
    fun testFilteringAndGetAVG() {
        val testWeatherData1 = WeatherData("03.01.2020",	"BHM", 10.0,20.0,5.0,4.33)
        val testWeatherData2 = WeatherData("17.01.2020",	"BHM", 20.0,30.0,10.0,9.33)
        val testWeatherData3 = WeatherData("22.01.2020",	"BHM", 60.0,60.0,60.0,0.0)
        val testWeatherData4 = WeatherData("30.01.2020",	"BHM", 30.0,25.0,3.0,7.33)

        val testWeatherData5 = WeatherData("17.01.2020",	"DEF", 60.0,60.0,60.0,16.0)
        val testWeatherData6 = WeatherData("17.01.2020",	"ABC", 60.0,60.0,60.0,0.0)

        val testWeatherData10 = WeatherData("22.08.2020",	"BHM", 60.0,60.0,60.0,0.0)
        val testWeatherData11 = WeatherData("22.03.2020",	"BHM", 60.0,60.0,60.0,0.0)
        val testWeatherData12 = WeatherData("22.04.2020",	"BHM", 60.0,60.0,60.0,0.0)
        val testWeatherData13 = WeatherData("22.05.2020",	"BHM", 60.0,60.0,60.0,0.0)
        val testWeatherData14 = WeatherData("22.06.2020",	"BHM", 60.0,60.0,60.0,0.0)
        val testWeatherData15 = WeatherData("22.07.2020",	"BHM", 60.0,60.0,60.0,0.0)


        // Create a list of WeatherData objects
        //val testWeatherDataList = listOf(testWeatherData1, testWeatherData2, testWeatherData3,testWeatherData4,testWeatherData5,testWeatherData6 )
        // Create a list of WeatherData objects
        val testWeatherDataList = listOf(testWeatherData1, testWeatherData2, testWeatherData3,testWeatherData4,testWeatherData5,testWeatherData6,testWeatherData10,testWeatherData11,testWeatherData12,testWeatherData13,testWeatherData14,testWeatherData15  )

        // Filter the list using the filterWeatherDataList function
        val filteredWeatherDataList = filterWeatherDataList(testWeatherDataList)

        //Check if Size of DataSet is still the Same
        Assertions.assertEquals(12, filteredWeatherDataList.size)




        //Check if Values which have been 60.0,60.0,60.0,0.0 are now 0.0,0.0,0.0,0.0
        val testWeatherData6_toTest = WeatherData("17.01.2020",	"ABC", 0.0,0.0,0.0,0.0)
        Assertions.assertEquals(filteredWeatherDataList[5], testWeatherData6_toTest)

        val testWeatherData3_ToTest = WeatherData("22.01.2020",	"BHM", 0.0,0.0,0.0,0.0)
        //println(filteredWeatherDataList[2])
        Assertions.assertEquals(filteredWeatherDataList[2], testWeatherData3_ToTest)


        //GET STATION LIST
        val inputCsvFilePathStations1 = "src/test/resources/stations_toTest_AVG_MIN.csv" // Replace with your input file path
        val stationDataList1 = readStationsFromCSV(inputCsvFilePathStations1)
        Assertions.assertEquals(1, stationDataList1.size)

        println()
        println("Check if the filtered elements 60,60,60,0 -> 0,0,0,0 not effect the avg")
        println()
        println("Normal Calculated AVG")
        var sumforAvg = 0.0;
        var rowCounter = 0;
        for(row in filteredWeatherDataList) {
            if(row.weatherStation.equals("BHM")){
                sumforAvg +=row.temperatureAvg;
                rowCounter +=1;
                print(row.temperatureAvg)
                print(" + ")
            }
        }
        print(" / ${rowCounter} = ")
        println(sumforAvg/rowCounter)
        var normalcalculatedAVG = sumforAvg/rowCounter

        val stationAVGRemoved = getAVGofStation(filteredWeatherDataList,stationDataList1)
        //Assertions.assertEquals(3, stationAVGRemoved.size)
        printStationStatistics(stationAVGRemoved)

        //Check if DEFAULT values get filtered out of calculation
        //Iterative vs Function getAVGofStation(..)
        Assertions.assertNotEquals(normalcalculatedAVG, stationAVGRemoved.get(0).temperatureAvg)
        Assertions.assertTrue(normalcalculatedAVG < stationAVGRemoved.get(0).temperatureAvg)











        //______________________________________________________________________________________________


    }

    @Test
    fun testFilteringTimePeriod() {
        val inputCsvFilePath = "src/test/resources/weather_inColums_toTest.csv" // Replace with your input file path

        val weatherDataList = readWeatherDataFromCSV(inputCsvFilePath)
        println("Before Filter " + weatherDataList.size)
        Assertions.assertEquals(24, weatherDataList.size)


        //TimePeriod 1_______________________________________________________
        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
        val startDate = dateFormat.parse("03.01.2020")
        val endDate = dateFormat.parse("03.01.2020")

        var weatherDataListFilteredTimePeriod =  filterWeatherDataForTimePeriod(weatherDataList, startDate, endDate)
        println("After Filter 03.01.2020-03.01.2020: " + weatherDataListFilteredTimePeriod.size)
        Assertions.assertEquals(6, weatherDataListFilteredTimePeriod.size)

        //TimePeriod 2_______________________________________________________
        //val dateFormat = SimpleDateFormat("dd.MM.yyyy")
        val startDate2 = dateFormat.parse("03.01.2020")
        val endDate2 = dateFormat.parse("17.01.2020")

        var weatherDataListFilteredTimePeriod2 =  filterWeatherDataForTimePeriod(weatherDataList, startDate2, endDate2)
        println("After Filter 03.01.2020-17.01.2020: " + weatherDataListFilteredTimePeriod2.size)
        Assertions.assertEquals(12, weatherDataListFilteredTimePeriod2.size)

        //TimePeriod 3_________________________________________________________
        //val dateFormat = SimpleDateFormat("dd.MM.yyyy")
        val startDate3 = dateFormat.parse("03.01.1990")
        val endDate3 = dateFormat.parse("17.01.1999")

        var weatherDataListFilteredTimePeriod3 =  filterWeatherDataForTimePeriod(weatherDataList, startDate3, endDate3)
        println("After Filter 03.01.1990-17.01.1999 (out of Range): " + weatherDataListFilteredTimePeriod3.size)
        Assertions.assertEquals(0, weatherDataListFilteredTimePeriod3.size)



    }

    @Test
    fun testReadWeatherDataFromCSV() {
        val inputCsvFilePath = "src/test/resources/weather_inColums_toTest.csv" // Replace with your input file path

        val weatherDataList = readWeatherDataFromCSV(inputCsvFilePath)
        println("Before Filter " + weatherDataList.size)
        Assertions.assertEquals(24, weatherDataList.size)
    }

    @Test
    fun testConcatenateInput(){
        val inputCsvFilePathStations1 = "src/test/resources/stations1_toTest.csv" // Replace with your input file path
        val inputCsvFilePathStations2 = "src/test/resources/stations2_toTest.csv" // Replace with your input file path
        val stationDataList1 = readStationsFromCSV(inputCsvFilePathStations1)
        val stationSize1 = stationDataList1.size
        Assertions.assertEquals(3, stationDataList1.size)


        //Stationlist 2___________
        val stationDataList2 = readStationsFromCSV(inputCsvFilePathStations2)
        val stationSize2 = stationDataList2.size
        Assertions.assertEquals(4, stationDataList2.size)


        //Print the Concatenated List
        println("stationSize1: ${stationSize1} + stationSize2: ${stationSize2}")
        val concatenatedStationList = stationDataList2 + stationDataList1
        Assertions.assertEquals(7, concatenatedStationList.size)

    }


    @Test
    fun testGetAVGofStation() {
        val inputCsvFilePath = "src/test/resources/weather_inColums_toTest.csv" // Replace with your input file path
        val weatherDataList = readWeatherDataFromCSV(inputCsvFilePath)
        Assertions.assertEquals(24, weatherDataList.size)

        val inputCsvFilePathStations1 = "src/test/resources/stations1_toTest.csv" // Replace with your input file path
        val inputCsvFilePathStations2 = "src/test/resources/stations2_toTest.csv" // Replace with your input file path

        val stationDataList1 = readStationsFromCSV(inputCsvFilePathStations1)
        Assertions.assertEquals(3, stationDataList1.size)

        val stationDataList2 = readStationsFromCSV(inputCsvFilePathStations2)
        Assertions.assertEquals(4, stationDataList2.size)

        print("stationSize1 + stationSize2: ")
        println(stationDataList1.size + stationDataList2.size)

        val concatenatedStationList = stationDataList2 + stationDataList1
        Assertions.assertEquals(7, concatenatedStationList.size)

        //________________________________________________________________________________

        val stationAVGRemoved = getAVGofStation(weatherDataList, concatenatedStationList)
        printStationStatistics(stationAVGRemoved)
        println()
        println("Print AVG values of Biel / CH at Station calculation above" )
        print("(${weatherDataList.get(20).temperatureAvg} + ${weatherDataList.get(21).temperatureAvg} + ${weatherDataList.get(22).temperatureAvg} + ${weatherDataList.get(23).temperatureAvg}) /4 = "  )
        val temperatureAvgBIEL = weatherDataList.get(20).temperatureAvg + weatherDataList.get(21).temperatureAvg +weatherDataList.get(22).temperatureAvg + weatherDataList.get(23).temperatureAvg
        println(temperatureAvgBIEL/4)
        Assertions.assertEquals(6.75, temperatureAvgBIEL/4)

        println()
        println("Print AVG of State Alaska = AK at States calculation below (value is rounded)")
        print("(${stationAVGRemoved.get(0).temperatureAvg} + ${stationAVGRemoved.get(1).temperatureAvg}) /2 = ")
        println((stationAVGRemoved.get(0).temperatureAvg+ stationAVGRemoved.get(1).temperatureAvg)/2)
        val stateAVGRemoved = getAVGofState(stationAVGRemoved)
        printStateStatistics(stateAVGRemoved)

        //Sorting Promt______________________________________________________________________
/*
        val sortedStationAVGRemoved = sortStationStatistics(stationAVGRemoved)
        printStationStatistics(sortedStationAVGRemoved)

        val sortedStateAVGRemoved = sortStationStatistics(stateAVGRemoved)
        printStateStatistics(sortedStateAVGRemoved)

 */


    }


    /*
    private var test: DoubleCase? = null

    @BeforeEach
    fun setUp() {
        test = DoubleCase()
    }



    @Test
    fun testDoubleCase() {
        Assertions.assertEquals("", doubleCase(""))
        Assertions.assertEquals("Aa", doubleCase("a"))
        Assertions.assertEquals("Aa", doubleCase("A"))
        Assertions.assertEquals("AaBb", doubleCase("aB3"))
        Assertions.assertEquals("", doubleCase("   . . . . "))
        Assertions.assertEquals("AaAaAaAa", doubleCase("%%%A A* A* A*"))
    }


    @Test
    fun testDoubleCaseRec() {
        Assertions.assertEquals("", doubleCaseRec(""))
        Assertions.assertEquals("Aa", doubleCaseRec("a"))
        Assertions.assertEquals("Aa", doubleCaseRec("A"))
        Assertions.assertEquals("AaBb", doubleCaseRec("aB"))
        Assertions.assertEquals("", doubleCaseRec("   . . . . "))
        Assertions.assertEquals("AaAaAaAa", doubleCaseRec("%%%A A* A* A*"))
    }
*/






}


