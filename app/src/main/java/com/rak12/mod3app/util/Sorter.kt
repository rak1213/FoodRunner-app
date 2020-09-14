package com.rak12.mod3app.util

import com.rak12.mod3app.model.Restaurant

class Sorter {
    companion object {
        var costComparator = Comparator<Restaurant> { r1, r2 ->
            val c1 = r1.cost
            val c2 = r2.cost
            if (c1.compareTo(c2) == 0) {
                ratingComparator.compare(r1, r2)
            } else {
                c1.compareTo(c2)
            }
        }

        var ratingComparator = Comparator<Restaurant> { r1, r2 ->
            val rating1 = r1.rating
            val rating2 = r2.rating
            if (rating1.compareTo(rating2) == 0) {
                val c1 = r1.cost
                val c2 = r2.cost
                c1.compareTo(c2)
            } else {
                rating1.compareTo(rating2)
            }
        }
    }
}