import java.io.BufferedReader
import java.io.FileReader
import java.io.FileWriter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
/*
import kotlin.collections.averageBy
import kotlin.collections.maxOfOrNull
import kotlin.collections.minOfOrNull

 */

//var counter=0

data class WeatherData(
    val timestamp: String,
    val weatherStation: String,
    val temperatureAvg: Double,
    val temperatureMax: Double,
    val temperatureMin: Double,
    val windSpeed: Double
)

data class Station(
    val city: String,
    val code: String,
    val state: String,
    val usps: String
)

data class StationStatistics(
    val stationCode: String,
    val temperatureAvg: Double,
    val temperatureMax: Double,
    val temperatureMin: Double,
    val windSpeedAvg: Double,
    val state: String

)

/*
data class StateStatistics(
    val stateCode: String,
    val temperatureAvg: Double,
    val temperatureMax: Double,
    val temperatureMin: Double,
    val windSpeedAvg: Double
)

 */

fun filterWeatherDataList(weatherDataList: List<WeatherData>): List<WeatherData> {
    return weatherDataList.map { data ->
        if (data.temperatureAvg == 60.0 &&
            data.temperatureMax == 60.0 &&
            data.temperatureMin == 60.0 &&
            data.windSpeed == 0.0
        ) {
            // Set all values to 0.0

            //_________________________
            //counter = counter+1
            //println(counter)
            //_________________________
            data.copy(
                temperatureAvg = 0.0,
                temperatureMax = 0.0,
                temperatureMin = 0.0,
                windSpeed = 0.0
            )


        } else {
            data
        }
    }
}

/*
fun filterWeatherDataForTimePeriod(
    weatherDataList: List<WeatherData>,
    startDate: String,
    endDate: String
): List<WeatherData> {
    // Define a SimpleDateFormat for date comparison
    val dateFormat = SimpleDateFormat("dd.MM.yyyy")

    try {
        // Parse the start and end dates
        val parsedStartDate = dateFormat.parse(startDate)
        val parsedEndDate = dateFormat.parse(endDate)

        // Filter the data based on the date range
        return weatherDataList.filter { data ->
            val dataDate = dateFormat.parse(data.timestamp)
            dataDate in parsedStartDate..parsedEndDate
        }
    } catch (e: ParseException) {
        // Handle date parsing errors, if needed
        e.printStackTrace()
        return emptyList()
    }
}

 */


fun filterWeatherDataForTimePeriod(
    weatherDataList: List<WeatherData>,
    startDate: Date,
    endDate: Date
): List<WeatherData> {
    return weatherDataList.filter { data ->
        val dataDate = SimpleDateFormat("dd.MM.yyyy").parse(data.timestamp)
        dataDate in startDate..endDate
    }
}
/*
fun getUserInputForTimePeriod(): Pair<Date, Date> {
    val scanner = Scanner(System.`in`)
    var startDate: Date? = null
    var endDate: Date? = null

    while (startDate == null || endDate == null || startDate > endDate) {
        println("Enter the start date (dd.MM.yyyy) between 03.01.2020 to 01.01.2021:")
        val startDateString = scanner.nextLine()
        println("Enter the end date (dd.MM.yyyy) between 03.01.2020 to 01.01.2021:")
        val endDateString = scanner.nextLine()

        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
        startDate = dateFormat.parse(startDateString)
        endDate = dateFormat.parse(endDateString)

        if (startDate == null || endDate == null || startDate >= endDate) {
            println("Invalid date range. Please make sure the end date is after the start date.")
        }
    }

    return Pair(startDate, endDate)
}


 */
fun getUserInputForTimePeriod(): Pair<Date, Date> {
    val scanner = Scanner(System.`in`)
    var startDate: Date? = null
    var endDate: Date? = null
    val dateFormat = SimpleDateFormat("dd.MM.yyyy")
    dateFormat.isLenient = false  // Enforce strict date parsing

    while (startDate == null || endDate == null || startDate > endDate) {
        println("Enter the START date (dd.MM.yyyy) between 03.01.2020 to 01.01.2021:")
        println("(a date before 03.01.2020 will automatically start at 03.01.2020 with the calculation)")
        val startDateString = scanner.nextLine()
        println("Enter the END date (dd.MM.yyyy) between 03.01.2020 to 01.01.2021:")
        println("(a date after 01.01.2021 will automatically end at 01.01.2021 with the calculation)")
        val endDateString = scanner.nextLine()

        try {
            startDate = dateFormat.parse(startDateString)
            endDate = dateFormat.parse(endDateString)

            if (startDate == null || endDate == null || startDate > endDate) {
                println("Invalid date range. Please make sure the end date is after the start date.")
                println("")

            }
        } catch (e: ParseException) {
            println("Invalid date format. Please enter the date in dd.MM.yyyy format.")
            println("")

        }
    }

    return Pair(startDate, endDate)
}


fun isValidTimestampFormat(timestamp: String): Boolean {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy")
    dateFormat.isLenient = false
    return try {
        dateFormat.parse(timestamp)
        true
    } catch (e: Exception) {
        false
    }
}


fun readWeatherDataFromCSV(inputFilePath: String): List<WeatherData> {
    val weatherDataList = mutableListOf<WeatherData>()

    try {
        val reader = BufferedReader(FileReader(inputFilePath))
        var line: String?

        // Read and process each line of the CSV file
        while (reader.readLine().also { line = it } != null) {
            // Split the line into fields using a delimiter (e.g., semicolon)
            val fields = line!!.split(";")

            // Check if there are fewer than 6 fields in the line
            if (fields.size < 6) {
                continue // Skip lines with fewer than 6 fields
            }

            val timestamp = fields[0].trim()

            // Check if the timestamp is in a valid SimpleDateFormat format
            if (!isValidTimestampFormat(timestamp)) {
                continue // Skip lines with invalid timestamps
            }

            val weatherStation = fields[1].trim()
            val temperatureAvg = convertToDouble(fields[2].trim()) ?: 60.0
            val temperatureMax = convertToDouble(fields[3].trim()) ?: 60.0
            val temperatureMin = convertToDouble(fields[4].trim()) ?: 60.0
            val windSpeed = convertToDouble(fields[5].trim()) ?: 0.0

            // Add the data to the WeatherData list
            weatherDataList.add(
                WeatherData(
                    timestamp = timestamp,
                    weatherStation = weatherStation,
                    temperatureAvg = temperatureAvg,
                    temperatureMax = temperatureMax,
                    temperatureMin = temperatureMin,
                    windSpeed = windSpeed
                )
            )
        }
        reader.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return weatherDataList
}

fun readStationsFromCSV(inputFilePath: String): List<Station> {
    val stationList = mutableListOf<Station>()

    try {
        val reader = BufferedReader(FileReader(inputFilePath))
        var line: String?

        // Read and process each line of the CSV file
        while (reader.readLine().also { line = it } != null) {
            // Split the line into fields using a delimiter (e.g., tab)
            val fields = line!!.split(";")

            // Check if there are exactly 4 fields in the line
            if (fields.size == 4) {
                val city = fields[0].trim()
                val code = fields[1].trim()
                val state = fields[2].trim()
                val usps = fields[3].trim()

                // Check if any field contains ".", "!", "?", or "$" and skip the line if any field matches
                if (city.contains('.') || city.contains('!') || city.contains('?') || city.contains('$') ||
                    code.contains('.') || code.contains('!') || code.contains('?') || code.contains('$') ||
                    state.contains('.') || state.contains('!') || state.contains('?') || state.contains('$') ||
                    usps.contains('.') || usps.contains('!') || usps.contains('?') || usps.contains('$')
                ) {
                    continue // Skip lines with any field containing ".", "!", "?", or "$"
                }

                // Add the data to the Station list
                stationList.add(
                    Station(
                        city = city,
                        code = code,
                        state = state,
                        usps = usps
                    )
                )
            } else {
                // Handle incorrect data format if needed
                // You can choose to skip or handle such lines differently
                println("Skipping line: $line")
            }
        }
        reader.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return stationList
}



fun getAVGofStation(weatherDataList: List<WeatherData>, stationList: List<Station>): List<StationStatistics> {
    val filteredWeatherData = weatherDataList.filter { weatherData ->
        stationList.any { it.code == weatherData.weatherStation }
    }
    val stationStatistics = stationList.mapNotNull { station ->
        val stationDataList = filteredWeatherData.filter { it.weatherStation == station.code }

        // Filtering out data with all values as 0.0
        val validStationDataList = stationDataList.filter { data ->
            data.temperatureAvg != 0.0 || data.temperatureMax != 0.0 || data.temperatureMin != 0.0 || data.windSpeed != 0.0
        }

        if (validStationDataList.isNotEmpty()) {

            /*
            val avgTemperature = validStationDataList.map { it.temperatureAvg }.average()
            val maxTemperature = validStationDataList.map { it.temperatureMax }.maxOrNull() ?: 0.0
            val minTemperature = validStationDataList.map { it.temperatureMin }.minOrNull() ?: 0.0
            val avgWindSpeed = validStationDataList.map { it.windSpeed }.average()


             */

            val avgTemperature = validStationDataList.map { it.temperatureAvg }.average()

            val maxTemperature = validStationDataList.let { list ->
                list.maxOfOrNull { it.temperatureMax } ?: 0.0
            }

            val minTemperature = validStationDataList.let { list ->
                list.minOfOrNull { it.temperatureMin } ?: 0.0
            }

            val avgWindSpeed = validStationDataList.map { it.windSpeed }.average()

            StationStatistics(
                station.code,
                avgTemperature,
                maxTemperature,
                minTemperature,
                avgWindSpeed,
                station.usps
            )
        } else {
            null // Return null for stations with no valid data
        }
    } // Filter out null entries

    return stationStatistics
}


//Used the Same Class StationStatistics - To us the same WriteFunction to CSV
fun getAVGofState(stationStatistics: List<StationStatistics>): List<StationStatistics> {
    // Calculate the same statistics for all stations in a state
    val stateStatistics = stationStatistics.groupBy { it.state }
        .map { (stateCode, stateDataList) ->
            val state = stateDataList.map { it.state}.distinct()
            val maxTemperature = stateDataList.maxOfOrNull { it.temperatureMax } ?: 0.0
            val minTemperature = stateDataList.minOfOrNull { it.temperatureMin } ?: 0.0
            val avgTemperature = if (stateDataList.any { it.temperatureAvg.isNaN() }) {
                0.0 // Handle NaN values by providing a default value
            } else {
                stateDataList.map { it.temperatureAvg }.average()
            }
            val avgWindSpeed = stateDataList.map { it.windSpeedAvg }.average()

            StationStatistics(
                stateCode,
                avgTemperature,
                maxTemperature,
                minTemperature,
                avgWindSpeed,
                state[0],
            )
        }
    return stateStatistics
}


fun sortStationStatistics(stationStatistics: List<StationStatistics>) : List<StationStatistics> {
//Promt for Sorting Stations:______________________________________________
    val scanner = Scanner(System.`in`)
    var choice: Int

    do {
        println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -")
        println("How would you like the sort the Station/State Data")
        println("1. By Station/State Name")
        println("2. By Average Temperature")
        println("3. By Max Temperature")
        println("4. By Min Temperature")
        println("5. By Average Wind Speed")

        if (scanner.hasNextInt()) {
            choice = scanner.nextInt()
            if (choice < 1 || choice > 5) {
                println("Not a valid sorting, please choose again.")
            }
        } else {
            println("Invalid input. Please enter a number between 1 and 5.")
            scanner.next() // Consume the invalid input
            choice = 0
        }
    } while (choice < 1 || choice > 5)


// Sort the data based on the user's choice
    val sortedAVGRemoved = when (choice) {
        1 -> stationStatistics.sortedBy { it.stationCode }
        2 -> stationStatistics.sortedBy { it.temperatureAvg }
        3 -> stationStatistics.sortedBy { it.temperatureMax }
        4 -> stationStatistics.sortedBy { it.temperatureMin }
        5 -> stationStatistics.sortedBy { it.windSpeedAvg }
        else -> stationStatistics
    }
    return sortedAVGRemoved
}

fun printStationStatistics(stationStatistics: List<StationStatistics>) {
    // Print table header
    println()
    println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -")
    println("Station calculation")
    println("calculated avg, max & min temperature from specific stations read in from CSV")
    println()
    println("%-10s | %-20s | %-15s | %-15s | %-20s".format(
        "Station", "Average Temperature", "Max Temperature", "Min Temperature", "Average Wind Speed"
    ))

    // Print table rows
    stationStatistics.forEach { stats ->
        println("%-10s | %-20.2f | %-15.2f | %-15.2f | %-20.2f".format(
            stats.stationCode, stats.temperatureAvg, stats.temperatureMax, stats.temperatureMin, stats.windSpeedAvg
        ))
    }
}



fun printStateStatistics(stats: List<StationStatistics>, includeState: Boolean = false) {
    // Define the table header
    val header = if (includeState) {
        "%-10s | %-20s | %-15s | %-15s | %-20s | %-15s"
            .format("State", "Average Temperature", "Max Temperature", "Min Temperature", "Average Wind Speed", "State")
    } else {
        "%-10s | %-20s | %-15s | %-15s | %-20s"
            .format("State", "Average Temperature", "Max Temperature", "Min Temperature", "Average Wind Speed")
    }

    println()
    println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -")
    println("States calculation")
    println("calculated avg, max & min temperature from States specific stations read in from CSV")
    println()
    // Print the table header
    println(header)

    // Print the table rows
    stats.forEach { stat ->
        val row = if (includeState) {
            "%-10s | %-20.2f | %-15.2f | %-15.2f | %-20.2f | %-15s"
                .format(stat.stationCode, stat.temperatureAvg, stat.temperatureMax, stat.temperatureMin, stat.windSpeedAvg, stat.state)
        } else {
            "%-10s | %-20.2f | %-15.2f | %-15.2f | %-20.2f"
                .format(stat.stationCode, stat.temperatureAvg, stat.temperatureMax, stat.temperatureMin, stat.windSpeedAvg)
        }
        println(row)
    }
}

fun writeWeatherDataToCSV(outputFilePath: String, weatherDataList: List<WeatherData>) {
    try {
        val writer = FileWriter(outputFilePath, true)

        // Write CSV header to the output file
        writer.write("Timestamp,WeatherStation,TemperatureAvg,TemperatureMax,TemperatureMin,WindSpeed\n")

        // Write each WeatherData object to the output CSV file
        for (weatherData in weatherDataList) {
            writer.write(
                "${weatherData.timestamp},${weatherData.weatherStation},"  +
                        "${weatherData.temperatureAvg},${weatherData.temperatureMax}," +
                        "${weatherData.temperatureMin},${weatherData.windSpeed}\n"
            )
        }
        writer.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }}


fun writeAVGStationDataToCSV(outputFilePath: String, stationDataList: List<StationStatistics>) {
    try {
        val writer = FileWriter(outputFilePath, true)

        // Write CSV header to the output file
        writer.write("WeatherStation,State,TemperatureAvg,TemperatureMax,TemperatureMin,WindSpeed\n")

        // Write each WeatherData object to the output CSV file
        for (stationData in stationDataList) {
            writer.write(
                "${stationData.stationCode},${stationData.state},"  +
                        "${stationData.temperatureAvg},${stationData.temperatureMax}," +
                        "${stationData.temperatureMin},${stationData.windSpeedAvg}\n"
            )
        }
        writer.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun convertToDouble(value: String?): Double? {
    return value?.toDoubleOrNull()
}





fun main() {
    val inputCsvFilePath = "src/main/resources/weather_inColums.csv" // Replace with your input file path
    val outputCsvFilePath = "src/main/resources/output_weather.csv" // Replace with your output file path
    //val outputCsvFilePath2 = "src/main/resources/output_weatherRemoved.csv" // Replace with your output file path


    //Filter Sets Values to Zero - not Filter the Records out_________________________________________________________________________________________________________________
    val weatherDataList = readWeatherDataFromCSV(inputCsvFilePath)
    println("Before Filter " + weatherDataList.size)

    //When Placing the Filter after the First Write To Outpufunction (L. 487) then you can check if the values get filtered correctly
    //Result stays the Same - and like this no Aggregate Error from Station
    var weatherDataListFiltered= filterWeatherDataList(weatherDataList)
    println("AfterFilter " + weatherDataListFiltered.size)

    println(weatherDataList[0])
    //for(row in weatherDataListFiltered){
    //    println(row)
    //}


    var count = 0

    for (weatherData in weatherDataListFiltered) {
        if (weatherData.temperatureAvg == 0.0 &&
            weatherData.temperatureMax == 0.0 &&
            weatherData.temperatureMin == 0.0 &&
            weatherData.windSpeed == 0.0
        ) {
            count++
        }
    }

    println("Number of occurrences with all values as 0.0: $count")




    //Write Filtered Data To Output___________________________________________________________________________________
    writeWeatherDataToCSV(outputCsvFilePath, weatherDataListFiltered)
    println("Data processing complete. Output written to $outputCsvFilePath")
    //_______________________________________________________________________________________________________________



/*
    //Remove Data from Filtered WeatherDataList
    val weatherDataListRemoved= removeFilteredDataFromWeatherDataList(weatherDataListFiltered)
    println("AfterFilter " + weatherDataListRemoved.size)

    //Write Filtered Data To Output___________________________________________________________________________________
    writeWeatherDataToCSV(outputCsvFilePath, weatherDataListRemoved)
    println("Data processing complete. Output written to $outputCsvFilePath2")
    //_______________________________________________________________________________________________________________

 */

    //Print first 10 Elements of the Read in Dataset
    for ((index, data) in weatherDataList.withIndex()) {
        if (index < 10) {
            println("Element $index: Timestamp=${data.timestamp}, Station=${data.weatherStation}, Avg Temp=${data.temperatureAvg}, Max Temp=${data.temperatureMax}")
        } else {
            break  // Exit the loop after printing the first 10 elements
        }
    }


    //Read in Station Files and Concatenate____________________________________________________________________________
    val inputCsvFilePathStations1 = "src/main/resources/stations1.csv" // Replace with your input file path
    val inputCsvFilePathStations2 = "src/main/resources/stations2.csv" // Replace with your input file path
    val stationDataList1 = readStationsFromCSV(inputCsvFilePathStations1)
    val stationSize1 = stationDataList1.size

    //Stationlist 2___________
    val stationDataList2 = readStationsFromCSV(inputCsvFilePathStations2)
    val stationSize2 = stationDataList2.size

    //Print the Concatenated List
    println("stationSize1 $stationSize1 + stationSize2 $stationSize2")
    val concatenatedStationList = stationDataList2 + stationDataList1
    for ((index, data) in concatenatedStationList.withIndex()) {
        println("Element $index: StationCity=${data.city}, StationCode=${data.code}, StationState=${data.state}, StationStateUSPS=${data.usps}")
    }

    /*
    //Get DataStructure - AVG from needed specific Station____________________________________________________________
    //Get Datastructure State - AVG from Stations in this State_____________________________________________________________
    val stationAVG = getAVGofStation(weatherDataListFiltered, concatenatedStationList)
    val stateAVG = getAVGofState(stationAVG)

    //Write Outputs to CSV Outputfile
    writeAVGStationDataToCSV(outputCsvFilePath, stationAVG)
    writeAVGStationDataToCSV(outputCsvFilePath, stateAVG)

     */


    //Filtered TimePeriod_______________________________________________________________________________________
    val scanner = Scanner(System.`in`)
    var userWantsToFilter = true

    while (userWantsToFilter) {
        // Ask the user if they want to filter by a specific time period
        println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -")
        println("Would you like to filter a specific time period from 03.01.2020 to 01.01.2021?")
        println("Press [y] for YES and enter in the next step a time period.")
        println("Press [anything else..] for NO, and calculate with the total data.")
        println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -")

        val userResponse = scanner.nextLine()

        if (userResponse.equals("y", ignoreCase = true)) {
            val (startDate, endDate) = getUserInputForTimePeriod()

            // Filter data for the specified time period
            val weatherDataListFilteredPeriod = filterWeatherDataForTimePeriod(weatherDataListFiltered, startDate, endDate)

            if (weatherDataListFilteredPeriod.isNotEmpty()) {
                println("Filtered data count: ${weatherDataListFilteredPeriod.size}")
                weatherDataListFiltered = weatherDataListFilteredPeriod
                // Perform further analysis on filtered data as needed
                userWantsToFilter = false // Exit the loop
            } else {
                println("No data found for the specified time period. Please enter a different time period.")
            }
        } else {
            userWantsToFilter = false // User chose not to filter, exit the loop
        }
    }


    //______________________________________________________________________________
    //Get DataStructure - AVG from needed specific Station____________________________________________________________
    //Get Datastructure State - AVG from Stations in this State_____________________________________________________________
    //if(filterTimePeriod){
    //    val stationAVGRemoved = getAVGofStation(weatherDataListFilteredPeriod, concatenatedStationList)
    //}
    val stationAVGRemoved = getAVGofStation(weatherDataListFiltered, concatenatedStationList)
    //val stationAVGRemoved = getAVGofStation(weatherDataListFiltered, concatenatedStationList)

    println(stationAVGRemoved[0])
    //printStationStatistics(stationAVGRemoved)

    val stateAVGRemoved = getAVGofState(stationAVGRemoved)
    //printStateStatistics(stateAVGRemoved)

    //Sorting Promt______________________________________________________________________

    val sortedStationAVGRemoved = sortStationStatistics(stationAVGRemoved)
    printStationStatistics(sortedStationAVGRemoved)

    val sortedStateAVGRemoved = sortStationStatistics(stateAVGRemoved)
    printStateStatistics(sortedStateAVGRemoved)
    //______________________________________________________________________



    //Write Outputs to CSV Outputfile
    writeAVGStationDataToCSV(outputCsvFilePath, stationAVGRemoved)
    writeAVGStationDataToCSV(outputCsvFilePath, stateAVGRemoved)




    //Zu analysierende Stations sind 71 - PTI(ungültig) = 70
    //PAQ und ILI ist Temp min = 0 korrekt
    //PTI FEHLT da der Wert immer Falsch war und somit herausgefiltert wird
}






/*
//Filter to filter them out -- Problems afterwards by analysing the station
fun filterWeatherDataList(weatherDataList: List<WeatherData>): List<WeatherData> {
    return weatherDataList.filter { data ->
        data.temperatureAvg != 60.0 ||
                data.temperatureMax != 60.0 ||
                data.temperatureMin != 60.0 ||
                data.windSpeed != 0.0
    }
}

 */

/*
fun removeFilteredDataFromWeatherDataList(weatherDataList: List<WeatherData>): List<WeatherData> {
    var filteredCount = 0

    val filteredData = weatherDataList.filter { data ->
        val shouldFilter = data.temperatureAvg == 0.0 &&
                data.temperatureMax == 0.0 &&
                data.temperatureMin == 0.0 &&
                data.windSpeed == 0.0

        if (shouldFilter) {
            filteredCount++
        }

        !shouldFilter
    }

    println("Filtered count: $filteredCount")

    return filteredData
}

 */

/*

fun readWeatherDataFromCSV(inputFilePath: String): List<WeatherData> {
    val weatherDataList = mutableListOf<WeatherData>()

    try {
        val reader = BufferedReader(FileReader(inputFilePath))
        var line: String?

        // Read and process each line of the CSV file
        while (reader.readLine().also { line = it } != null) {
            // Split the line into fields using a delimiter (e.g., semicolon)
            val fields = line!!.split(";")

            // Check if there are fewer than 6 fields in the line
            if (fields.size < 6) {
                // Replace missing fields with default values
                val timestamp = fields.getOrNull(0)?.trim() ?: ""
                val weatherStation = fields.getOrNull(1)?.trim() ?: ""
                val temperatureAvg = convertToDouble(fields.getOrNull(2)?.trim()) ?: 60.0
                val temperatureMax = convertToDouble(fields.getOrNull(3)?.trim()) ?: 60.0
                val temperatureMin = convertToDouble(fields.getOrNull(4)?.trim()) ?: 60.0
                val windSpeed = convertToDouble(fields.getOrNull(5)?.trim()) ?: 0.0

                // Add the data to the WeatherData list
                weatherDataList.add(
                    WeatherData(
                        timestamp = timestamp,
                        weatherStation = weatherStation,
                        temperatureAvg = temperatureAvg,
                        temperatureMax = temperatureMax,
                        temperatureMin = temperatureMin,
                        windSpeed = windSpeed
                    )
                )
            } else {
                // If there are 6 fields, use the existing values
                val timestamp = fields[0].trim()
                val weatherStation = fields[1].trim()
                val temperatureAvg = convertToDouble(fields[2].trim()) ?: 60.0
                val temperatureMax = convertToDouble(fields[3].trim()) ?: 60.0
                val temperatureMin = convertToDouble(fields[4].trim()) ?: 60.0
                val windSpeed = convertToDouble(fields[5].trim()) ?: 0.0

                // Add the data to the WeatherData list
                weatherDataList.add(
                    WeatherData(
                        timestamp = timestamp,
                        weatherStation = weatherStation,
                        temperatureAvg = temperatureAvg,
                        temperatureMax = temperatureMax,
                        temperatureMin = temperatureMin,
                        windSpeed = windSpeed
                    )
                )
            }
        }

        reader.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return weatherDataList
}

 */

/*
fun readStationsFromCSV(inputFilePath: String): List<Station> {
    val stationList = mutableListOf<Station>()

    try {
        val reader = BufferedReader(FileReader(inputFilePath))
        var line: String?

        // Read and process each line of the CSV file
        while (reader.readLine().also { line = it } != null) {
            // Split the line into fields using a delimiter (e.g., tab)
            val fields = line!!.split(";")

            // Check if there are exactly 4 fields in the line
            if (fields.size == 4) {
                val city = fields[0].trim()
                val code = fields[1].trim()
                val state = fields[2].trim()
                val usps = fields[3].trim()

                // Add the data to the Station list
                stationList.add(
                    Station(
                        city = city,
                        code = code,
                        state = state,
                        usps = usps
                    )
                )
            } else {
                // Handle incorrect data format if needed
                // You can choose to skip or handle such lines differently
                println("Skipping line: $line")
            }
        }

        reader.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return stationList
}
 */


/*
fun getAVGofStation(weatherDataList: List<WeatherData>, stationList: List<Station>):List<StationStatistics>{
    // Filter weather data for stations in concatenatedStationList
    val filteredWeatherData = weatherDataList.filter { weatherData ->
        stationList.any { it.code == weatherData.weatherStation }
    }

// Calculate average temperature, absolute minimum and maximum, and average windspeed for each station
    val stationStatistics = stationList.map { station ->
        val stationDataList = filteredWeatherData.filter { it.weatherStation == station.code }
        val avgTemperature = stationDataList.map { it.temperatureAvg }.average()
        val maxTemperature = stationDataList.map { it.temperatureMax }.maxOrNull() ?: 0.0
        val minTemperature = stationDataList.map { it.temperatureMin }.minOrNull() ?: 0.0
        val avgWindSpeed = stationDataList.map { it.windSpeed }.average()
        //val state = stationDataList.map{ it.}

        StationStatistics(
            station.code,
            avgTemperature,
            maxTemperature,
            minTemperature,
            avgWindSpeed,
            station.usps
        )
    }
    // Print the station statistics
    stationStatistics.forEach { stats ->
        println("Station: ${stats.stationCode}")
        println("Average Temperature: ${stats.temperatureAvg}")
        println("Max Temperature: ${stats.temperatureMax}")
        println("Min Temperature: ${stats.temperatureMin}")
        println("Average Wind Speed: ${stats.windSpeedAvg}")
        println()
    }
    return stationStatistics
}


 */