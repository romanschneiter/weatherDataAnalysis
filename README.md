# weatherDataAnalysis


-Data gets read from csv and stored in data class WeatherData
-Data gets read and concatinated from csv for the specific stations which will be analysed

-the filterfunction sets only values with value 60.0, 60.0, 60.0, 0.0 to 0.0, 0.0, 0.0, 0.0  (Values with 60.0, 60.0, 60.0, 14.54 will get handled as a normal value for the calculation)
-there is an console interaction if the user want to analyse a specific time period additionaly in the given range from 03.01.2020 until 01.01.2021
-as default the hole data will be processed 

***-the calculations for avg temperature, min temperature, max temperature, avg windSpeed is done first for the Stations***
1. filter the the stations which should be analysed which matches the WeatherData
2. the values which we set already to 0.0, 0.0, 0.0, 0.0, will be ignored for the calculation
3. calculate per station avg temperature, min temperature, max temperature, avg windSpeed
4. return a new <List>StationStatistic
5. there is an console interation by which column the Station data should be sorted ( 1. By Station/State Name//2. By Average Temperature//3. By Max Temperature//4. By Min Temperature//5. By Average Wind Speed)
6. the print function prints a table structure 

***-and second for the State***
1.group the <List>StationStatistic by State
2.calcualate per state avg temperature, min temperature, max temperature, avg windSpeed
5. there is an console interation by which column the Station data should be sorted ( 1. By Station/State Name//2. By Average Temperature//3. By Max Temperature//4. By Min Temperature//5. By Average Wind Speed)
6. the prin function prints a table structure 


# Tests
-the filter function is tested if the values 60.0, 60.0, 60.0, will reset the values to 0.0 to 0.0, 0.0, 0.0, 0.0  
-the filter function is tested in combination with the getAVGofStation function that values which are set to 0.0 to 0.0, 0.0, 0.0, 0.0 not affect the avg calulation

-the filterWeatherDataForTimePeriod is tested that we can analyse a specific time period in the given range of data.

-the read functions for the csv are tested that the have the right amount of data as well as the concatenate function for the specific station files
-a further test is written to check if the values get calculated in the right way - examples as samples for avg is provided with the print function





