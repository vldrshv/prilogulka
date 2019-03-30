package com.example.prilogulka.data;

public class Districts {
    private String[] firstGroup = {
            "г. Москва", "г. Санкт-Петербург", "Ханты-Мансийский автономный округ",
            "Республика Татарстан", "Ямало-Ненецкий автономный округ", "Московская область", "Тюменская область",
            "Свердловская область", "Ленинградская область", "Краснодарский край", "Республика Башкортостан",
            "Самарская область", "Белгородская область", "Пермский край", "Красноярский край", "Воронежская область",
            "Сахалинская область", "Нижегородская область", "Липецкая область", "Ростовская область"
    };

    private String[] secondGroup = {
            "Республика Саха (Якутия)", "Челябинская область", "Калужская область", "Кемеровская область",
            "Республика Коми", "Приморский край", "Вологодская область", "Новосибирская область",
            "Иркутская область", "Курская область", "Тульская область", "Рязанская область",
            "Оренбургская область", "Волгоградская область", "Удмуртская Республика", "Мурманская область",
            "Хабаровский край", "Саратовская область", "Омская область", "Республика Крым",
            "Ставропольский край", "Архангельская область", "Владимирская область"
    };

    private String[] thirdGroup = {
            "Калининградская область", "Томская область", "Ярославская область", "Республика Дагестан",
            "Тверская область", "Ульяновская область", "Ненецкий автономный округ", "Амурская область",
            "Алтайский край", "Кировская область", "Тамбовская область", "Камчатский край",
            "Брянская область", "Пензенская область", "Чувашская Республика", "Астраханская область",
            "Новгородская область", "Смоленская область", "Магаданская область",
            "Чеченская Республика", "Республика Марий Эл"
    };

    private String[] fourthGroup = {
            "Забайкальский край", "г. Севастополь", "Республика Мордовия", "Ивановская область",
            "Орловская область", "Республика Бурятия", "Республика Хакасия", "Чукотский автономный округ",
            "Республика Карелия", "Курганская область", "Псковская область", "Республика Адыгея",
            "Костромская область", "Карачаево-Черкесская Республика", "Республика Северная Осетия – Алания",
            "Республика Калмыкия", "Кабардино-Балкарская Республика", "Республика Алтай",
            "Республика Ингушетия", "Республика Тыва", "Еврейская автономная область"
    };

    public double getDistrictCoefficient(String myDistrict) {
        if (contains(firstGroup, myDistrict))
            return 1.2;
        if (contains(secondGroup, myDistrict))
            return 1.0;
        if (contains(thirdGroup, myDistrict))
            return 0.9;
        if (contains(fourthGroup, myDistrict))
            return 0.8;

        return 1.0;
    }

    public float getLocationCoefficient(String myDistrict) {
        if (contains(firstGroup, myDistrict))
            return getCoefficient(firstGroup, myDistrict);
        if (contains(secondGroup, myDistrict))
            return getCoefficient(secondGroup, myDistrict) * firstGroup.length;
        if (contains(thirdGroup, myDistrict))
            return getCoefficient(thirdGroup, myDistrict) * firstGroup.length * secondGroup.length;
        if (contains(fourthGroup, myDistrict))
            return getCoefficient(fourthGroup, myDistrict) * firstGroup.length * secondGroup.length
                    * thirdGroup.length;

        return 0;
    }

    private boolean contains(String[] districtArr, String myDistrict) {

        for(String s : districtArr){
            if (myDistrict.contains(s))
                return true;
        }

        return false;
    }

    private float getCoefficient(String[] districtArr, String myDistrict) {
        for (int i = 0; i < districtArr.length; i++) {
            if (myDistrict.contains(districtArr[i]))
                return i + 1;
        }
        return 0;
    }
}
