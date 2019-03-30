package com.example.prilogulka.data

class Coefficient {
    var type: Int = 0
    var coef: Double = 0.toDouble()
    var categoriesList: ArrayList<Category>? = null

    constructor(type: Int, coef: Float, categories: IntArray) {
        this.type = type
        this.coef = coef.toDouble()
        this.categoriesList = makeCatList(categories)
    }

    constructor() {
        this.categoriesList = ArrayList<Category>()
    }

    fun <T> setCategoriesList(categories: T) {
        when(categories){
            is IntArray -> this.categoriesList = makeCatList(categories)
            is ArrayList<*> -> {
                val arr = IntArray(categories.size)
                for (i in categories.indices)
                    arr[i] = categories[i] as Int
                this.categoriesList = makeCatList(arr)
            }
        }
    }

    override fun toString(): String {
        return "coefs.Coefficient{" +
                "type=" + type +
                ", coef=" + coef +
                ", categoriesList=" + categoriesList.toString() +
                "}\n"
    }

    private fun makeCatList(categories: IntArray): ArrayList<Category> {
        val catList = ArrayList<Category>()
        if (categories.size % 2 != 0)
            throw IllegalArgumentException()

        var i = 0
        while (i < categories.size) {
            catList.add(Category(categories[i], categories[i + 1]))
            i += 2
        }

        return catList
    }

    inner class Category(var DOWN_category: Int, var UP_category: Int) {

        override fun toString(): String {
            return "Category{ $DOWN_category , $UP_category }\n"
        }
    }
}