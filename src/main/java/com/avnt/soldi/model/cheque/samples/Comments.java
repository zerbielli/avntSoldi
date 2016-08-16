package com.avnt.soldi.model.cheque.samples;

/**
 * Enum Comments contains samples of comments
 *
 * @version 1.0
 * @author Dmitry
 * @since 04.09.2015
 */
public enum Comments {
    comment1("С помощью тонкого паяльника и технического фена укрепляется пайка, восстанавливаются поврежденные контакты и дорожки. Неисправные детали заменяются. Что касается флешек-«утопленников», то с ними поступают аналогично другим гаджетам. Плата отмывается от солей и грязи, затем погружается в изопропиловый спирт (он вытесняет воду из щелей) и наконец, сушится теплым воздухом."),
    comment2("Основная причина - повреждение микропрограммы, или прошивки. Прошивка включает в себя неизменяемый микрокод контроллера и служебные данные во флеш-памяти, которые обновляются довольно часто. Другая причина отказов - сбои самой флеш-памяти. В каждый чип заложен резерв емкости для замены сбойных страниц. Дефект-менеджмент - одна из основных функций прошивки, и если интенсивность переназначений превышает порог, то ставится блокировка. Порой флешка блокируется только на запись: данные видны и читаются, но при попытках удаления файла или форматирования выдается сообщение «Диск защищен от записи». Память NAND повреждается при записи, а чтение в легких случаях можно и оставить."),
    comment3("При погнутом или отломанном USB-разъеме флешка неработоспособна, в лучшем случае опознается через раз и долго не проживет. Треснутая плата требует ремонта, не всегда успешного. Карта SD с вылетевшим ползунком становится read-only, ничего записать на нее нельзя. SD с расслоившимся корпусом бывает сложно вставить в слот и извлечь из него, применение силы ситуацию только ухудшает."),
    comment4("Механический ремонт имеет целью восстановить функционирование и надежность флешки. Это склейка или замена корпуса, подбор колпачка взамен потерянного и т.п. У расшатанного USB-разъема пропаиваются крепежные ушки и сами контакты. Погнутый разъем выправляется крайне осторожно: могут порваться соседние дорожки на плате, и ремонт осложняется. На картах SD вместо потерянного ползунка можно вклеить кусочек спички. Контакты чистятся ватной палочкой со спирто-бензиновой смесью. При работе надо избегать статики - карты к ней чувствительны.");

    private final String comment;
    Comments(String s) {comment = s;}
    public boolean equalsName(String otherComment) {return (otherComment != null) && comment.equals(otherComment);}
    public String toString() {return this.comment;}
}
