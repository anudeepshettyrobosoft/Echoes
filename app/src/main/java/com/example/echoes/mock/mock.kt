package com.example.echoes.mock

import com.example.echoes.domain.model.BadgeItem
import com.example.echoes.domain.model.NewsItem
import com.example.echoes.domain.model.ProfileData
import com.example.echoes.domain.model.Voucher

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

val mockVouchers = listOf(
    Voucher(
        id = "1",
        title = "Amazon ₹100 Voucher",
        description = "₹100 off on ₹500",
        pointsRequired = 100,
        type = "Amazon",
        code = "ECHOES-1001-XYZ"
    ),
    Voucher(
        id = "2",
        title = "Flipkart ₹200 Voucher",
        description = "₹200 off on ₹1000",
        pointsRequired = 200,
        type = "Flipkart",
        code = "ECHOES-1002-XYZ"
    ),
    Voucher(
        id = "3",
        title = "Uber ₹50 Discount",
        description = "₹50 off on next ride",
        pointsRequired = 50,
        type = "Uber",
        code = "ECHOES-1003-XYZ"
    ),
    Voucher(
        id = "4",
        title = "Myntra ₹100 Voucher",
        description = "₹100 off on ₹500",
        pointsRequired = 100,
        type = "Myntra",
        code = "ECHOES-1004-XYZ"
    ),
    Voucher(
        id = "5",
        title = "Ola ₹20 Voucher",
        description = "₹200 off on ₹1000",
        pointsRequired = 20,
        type = "Ola",
        code = "ECHOES-1005-XYZ"
    ),
    Voucher(
        id = "6",
        title = "Zepto ₹150 Discount",
        description = "₹50 off on next ride",
        pointsRequired = 150,
        type = "Zepto",
        code = "ECHOES-1006-XYZ"
    )
)
