package com.tenda.common.schema

data class Criteria(
    val filters: List<Filter> = emptyList(),
    val sort: List<Sort> = emptyList()
) {
    data class Filter(
        val field: String,
        val value: String,
        val condition: Condition = Condition.EQUAL
    )

    data class Sort(
        val field: String,
        val direction: Direction = Direction.ASC
    )
}
