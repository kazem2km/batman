package com.yara.common_test.datasets

import com.yara.model.Search
import java.util.*

object UserDataset {

    val DATE_REFRESH: Date = GregorianCalendar(2018, 5, 12).time
    val FAKE_USERS = listOf(
        Search(
            imdbID = "Id_1",
            Year = "Login_1",
            Title = "AvatarUrl_1",
            Poster = "Blog1",
            Type = "Company1",
            lastRefreshed = DATE_REFRESH
        ),
        Search(
            imdbID = "Id_2",
            Year = "Login_2",
            Title = "AvatarUrl_2",
            Poster = "Blog1",
            Type = "Company1",
            lastRefreshed = DATE_REFRESH
        ),
        Search(
            imdbID = "Id_3",
            Year = "Login_3",
            Title = "AvatarUrl_3",
            Poster = "Blog1",
            Type = "Company1",
            lastRefreshed = DATE_REFRESH
        )
    )
}