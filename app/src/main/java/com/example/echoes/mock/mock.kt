package com.example.echoes.mock

import com.example.echoes.domain.model.BadgeItem
import com.example.echoes.domain.model.NewsItem
import com.example.echoes.domain.model.ProfileData

val mockNewsList = listOf(
    NewsItem(
        id = "1",
        title = "Sample News 1",
        description = "This is a sample news description.",
        category = "General",
        status = "SUBMITTED",
        submittedDate = "1 Jan 2024",
        imageORVideoUrl = "https://www.titan.co.in/on/demandware.static/-/Sites-titan-master-catalog/default/dw87154230/images/Titan/Catalog/1733KM02_1.jpg",
        comments = ""
    ),
    NewsItem(
        id = "2",
        title = "Sample News 2",
        description = "This is another sample news description.",
        category = "Politics",
        status = "PUBLISHED",
        submittedDate = "1 Feb 2024",
        comments = "Fake news"
    ),
    NewsItem(
        id = "3",
        title = "Sample News 3",
        description = "This is a sample news description.",
        category = "General",
        status = "ACCEPTED",
        submittedDate = "1 Jan 2024",
        comments = ""
    ),
    NewsItem(
        id = "4",
        title = "Sample News 4",
        description = "This is another sample news description.",
        category = "Politics",
        status = "REJECTED",
        submittedDate = "1 Feb 2024",
        comments = "Fake news"
    ),
    NewsItem(
        id = "5",
        title = "Sample News 1",
        description = "This is a sample news description.",
        category = "General",
        status = "ACCEPTED",
        submittedDate = "1 Jan 2024",
        comments = ""
    ),
    NewsItem(
        id = "6",
        title = "Sample News 2",
        description = "This is another sample news description.",
        category = "Politics",
        status = "REJECTED",
        reportedTime = "1 Feb 2024",
        comments = "Fake news"
    )
)

val mockProfileData = ProfileData(
    name = "John Doe",
    doj = "1 Jan 2024",
   // profilePictureUrl = "https://www.titan.co.in/on/demandware.static/-/Sites-titan-master-catalog/default/dw87154230/images/Titan/Catalog/1733KM02_1.jpg",
    badges = listOf(
        BadgeItem(
           badgeType = "BRONZE",
            count = 5,
        ),
        BadgeItem(
            badgeType = "SILVER",
            count = 3,
        ),
        BadgeItem(
            badgeType = "GOLD",
            count = 1,
        )

    )
)