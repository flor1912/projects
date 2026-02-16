package com.example.myapplication


data class OsmElement(
    val type: String? = null,        // "node", "way", "relation"
    val id: Long? = null,
    val lat: Double? = null,         // present for nodes
    val lon: Double? = null,         // present for nodes
    val tags: Map<String, String>? = null
)

data class OverpassResponse(
    val elements: List<OsmElement> = emptyList()
)



class OverPassAPI {

}

fun buildOverpassQueryCityCountry(
    city: String,
    country: String,
    dietIndex: Int? = null,
    keywords: Set<String> = emptySet(),
    limit: Int = 200
): String {
    val dietFilter = when (dietIndex) {
        1 -> """["diet:vegetarian"="yes"]"""
        2 -> """["diet:vegan"="yes"]"""
        else -> ""
    }

    // Build regex like "pizza|sushi|vegan" (escaped)
    val keywordRegex = keywords
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .joinToString("|") { Regex.escape(it) }

    val keywordBlock = if (keywordRegex.isNotBlank()) {
        """
        (
          node["amenity"="restaurant"]$dietFilter["name"~"$keywordRegex",i](area.cityArea);
          node["amenity"="restaurant"]$dietFilter["cuisine"~"$keywordRegex",i](area.cityArea);
        );
        """
    } else {
        """
        (
          node["amenity"="restaurant"]$dietFilter(area.cityArea);
        );
        """
    }

    return """
        [out:json][timeout:25];
        area["name"="$country"]["boundary"="administrative"]["admin_level"="2"]->.countryArea;
        area["name"="$city"]["boundary"="administrative"](area.countryArea)->.cityArea;
        $keywordBlock
        out body $limit;
    """.trimIndent()
}

