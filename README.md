# GameApp
 job interview task

For local storage I implemented Shared preference for easy and simple way of storing small amount of data.
On app start I implemented whelcome message and start button. To skip onboarding I decidet to simpy check if there are
any genres already stored and depending of that app either goes to genre selector or games list.
I implemented settings as gear icon that again opens genre selector page to simplify app development. For fetching game 
data, I integrated Retrofit, a powerful networking library, to interact with the RAWG database. 
Retrofit simplifies the process of making API calls and processing the responses, ensuring smooth data retrieval 
for the app.

For displaying data I opted for RecyclerView to efficiently display large sets of data, and to increase preformance. 
For creating modern UI i decided to use Material design, and for displaying game details I opted for material card.
To display game images, I leveraged Picasso, a popular image loading library. Picasso efficiently handles image 
loading and caching, enhancing the app's performance when displaying game artwork.


