
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import org.languagetool.AnalyzedToken;
import org.languagetool.AnalyzedTokenReadings;
import org.languagetool.tagging.uk.UkrainianTagger;

import textanalysis.ng.GrammarMatch;
import textanalysis.ng.Parser;
import textanalysis.ng.ParserTokenizer;

import textanalysis.ng.grammars.Person;
import textanalysis.ng.preprocessors.DictionaryPreprocessor;
import textanalysis.ng.preprocessors.ParserTokenPreprocessor;

/**
 *
 * @author Lenovo
 */
public class NGParserTest {

//    @Test
    public void tagger() throws IOException {

        UkrainianTagger ut = new UkrainianTagger();
        List<String> sentence = new ArrayList();

        sentence.add("Степан");
        sentence.add("прийшов");

        List<AnalyzedTokenReadings> tag = ut.tag(sentence);

        for (AnalyzedTokenReadings tr : tag) {
            for (AnalyzedToken it : tr.getReadings()) {
                System.out.println(it.getPOSTag());
            }

            System.out.println("\n");

        }

    }

    private String getDefaultText() {

        String zuk = "Після того, як Марк розробив версію гри «Ризик» і написав програму «Synapse» (програма для музичного плеєра, яка сама аналізувала і систематизувала музичні інтереси свого користувача, створюючи під його смаки плейлисти), йому надійшло відразу два вигідних пропозиції про співробітництво з боку AOL і Microsoft, на які він відповів відмовою.\n"
                + "\n"
                + "У Гарварді юний хакер разом з Крісом Хьюзом і Дастіном Московіцем почав працювати над створенням сучасного дива — нової соціальної мережі «Facebook». Ця соціальна мережа спочатку була порталом для спілкування між студентами одного університету. Називалася ця мережа «The Face Book» (книга осіб). На сьогоднішній день «Facebook» є соціальною мережею глобального рівня. На її розробку свого часу знадобилися гроші. З початковим капіталом Марку допомогли його батьки. Фінансову підтримку надавав юним талантам і бразильський студент Едуардо Саверін. Згодом Цукерберг намагався усунути Саверіна від управління справами компанії, що спричинило за собою судовий розгляд у 2005 році. Цукерберг вдало знайомиться з співзасновником платіжної системи, бізнесменом Пітером Тиелом, який інвестує проект на 500 тисяч доларів.\n"
                + "\n"
                + "У 2009 році Цукерберг знайомиться з Юрієм Мільнером, співвласником Mail.ru Group і DST Global. Знайомство було дуже плідним і закінчилось грандіозним угодою: DST придбала 1,96 % акцій «Facebook» за 200 млн американських доларів.\n"
                + "\n"
                + "Цукерберг володіє 24 % «Facebook» і заробляє на цьому колосальне стан: він вважається наймолодшим мільярдером у світі. Про його стан ходять легенди. У березні 2010 року знаменитий журнал «Forbes» назвав Марка одним з наймолодших мільярдерів із капіталом 4 млрд доларів. У вересні того ж року цей же журнал публікує список найбагатших американців за минулий рік, в якому Цукерберг зайняв почесне 29-е місце, але його стан вже зазначалося в 7 млрд доларів. У 2011 році в списку найбагатших людей США Марк займає 14-е місце, збільшивши всього за рік свій стан до 17,5 млрд доларів.\n"
                + "\n"
                + "Слава Марка росте не по днях, а по годинах. У тому ж 2010 році ще один глянцевий і шанований у бомонді журнал «Time» назвав молодого мільярдера Людиною Року. 2010 рік став по-справжньому знаковим для Марка: у грудні він заявив, що приєднався до всесвітньо відомої «Клятві дарування», філантропічної кампанії, створеної мільярдерами Уорреном Баффеттом і Біллом Гейтсом. Це робить тільки честь молодому підприємцю, готовим жертвувати свої гроші на благо іншим. Він вже не раз був помічений у справах благодійності: жертвував гроші школам і розвитку соцмереж.\n"
                + "\n"
                + "Красиво жити не заборониш, і в 2011 році Марк купив собі будинок в Пало-Альто вартістю в 7 мільйонів доларів. Тут він і проживає.\n"
                + "\n"
                + "Восени 2012 року Марк Цукерберг побував у Росії, особисто зустрічався з Д. А. Медведєвим. За короткий термін перебування встиг взяти участь у телевізійних передачах на Першому каналі і виступити перед студентами МДУ з лекцією про себе та своєї компанії.\n"
                + "\n"
                + "За даними на 2012 рік, користувальницький кордон «Facebook» перевалив за мільярд";

        String poroh = "Порошенко Петро Олексійович, народився 26.09.1965р. в м.Болград Одеської області.\n"
                + "Громадянин України. Мешкає на території України з дня народження, в т.ч. протягом останніх 23-х років з дня проголошення Незалежності України.\n"
                + "Освіта вища:1982-1989-Київський університет ім.Т.Шевченка. Факультет міжнародних відносин і міжнародного права, спеціалізація: міжнародні економічні відносини.\n"
                + "1984-1986 - служба в армії.\n"
                + "1989-1992 - аспірант-асистент Кафедри міжнародних економічних відносин Київського університету ім.Т.Г.Шевченка.\n"
                + "1993-1998 - генеральний директор Концерну «Укрпромінвест».\n"
                + "1998-2002 - народний депутат України ІІІ скликання.\n"
                + "2000-2004 - заступник Голови Ради Національного Банку України.\n"
                + "2002-2005 - народний депутат України IV скликання. Голова Комітету ВРУ з питань бюджету.\n"
                + "У 2002 захистив наукову дисертацію «Правове регулювання управління державними корпоративними правами в Україні». Кандидат юридичних наук.\n"
                + "8.02.2005-8.09.2005 - Секретар Ради Національної безпеки і оборони України.\n"
                + "2006-2007 - народний депутат України V скликання, Голова Комітету ВРУ з питань фінансів і банківської діяльності.\n"
                + "2009-2010 - Міністр закордонних справ України.\n"
                + "2012 - Міністр економічного розвитку і торгівлі України.\n"
                + "2012 - народний депутат України VII скликання, співголова Комітету парламентського співробітництва Україна-ЄС.\n"
                + "1998-2003 - член Координаційної ради з питань функціонування ринку цінних паперів в Україні.\n"
                + "У 1998 заснував іменний Благодійний фонд, який здійснює благодійні проекти у різних сферах суспільного життя.\n"
                + "2000-2002 - керівник фракції «Солідарність» у ВРУ III скликання.\n"
                + "2007-2012-Голова Ради НБУ.\n"
                + "Заслужений економіст України, лауреат Державної премії України в галузі науки і техніки, лауреат міжнародної премії імені П.Орлика, нагороджений Орденом «За громадянські заслуги» ступені Великого Хреста Королівства Іспанії.\n"
                + "Позапартійний.\n"
                + "Одружений. Дружина - Порошенко Марина Анатолівна, сини – Олексій (1985 р.н.) і Михайло (2001 р.н.), доньки - Євгенія і Олександра (2000 р.н.)\n"
                + "Проживає за адресою: 01021, м. Київ, вул. Грушевського, буд.9, кв. 37\n"
                + "Судимості не має.\n"
                + "25 травня 2014 року на позачергових виборах Президента України був обраний Президентом України. 7 червня 2014 року склав присягу перед Українським народом як Глава держави.";

        String musk = "Ілoн Pівc Macк (aнгл. Elon Reeve Musk., нap. 28 чepвня 1971 poкy в Пpeтopії, ПAP) - кaнaдcькo-aмepикaнcький інжeнep, підпpиємeць, винaxідник і інвecтop; мільяpдep. Зacнoвник, влacник, гeнepaльний диpeктop і гoлoвний інжeнep SpaceX; гoлoвний дизaйнep, гeнepaльний диpeктop і глaвa paди диpeктopів Tesla Motors. Bxoдить дo paди диpeктopів кoмпaнії SolarCity, зacнoвaнoї йoгo двoюpідними бpaтaми.У peйтингy жypнaлy Forbes в 2015 poці йoгo cтaтки oцінюютьcя в $12 млpд.\n"
                + "\n"
                + "Народився Ілон Маск (Elon Musk) в 1971 році в Преторії, ПАР (Pretoria, South Africa). Мати його, канадка за походженням, була дієтологом, батько, південноафриканець, працював інженером. Свій перший комп'ютер Ілон отримав у віці 10 років і відразу ж приступив до програмування. В 12 років він заробив свої перші $ 500, продавши 'Blastar' - гру власної розробки. Після школи Ілон, який не горів бажанням йти в армію ПАР, виїхав з країни і відправився до родичів з боку матері в Канаду (Canada). До речі, сам Ілон мріяв про США, називаючи Штати 'країною великих можливостей'. \n"
                + "Два роки він провчився в Queen's University в Кінгстоні, Онтаріо (Kingston, Ontario), після чого його мрія збулася - Маск відправився в США по стипендії від Університету Пенсільванії (University of Pennsylvania). Вивчав він фізику і бізнес, і після навчання Ілон Маск зробив свій вибір, вирішивши, чим би він хотів по-справжньому займатися - це були Інтернет (тільки з'являвся), чиста енергія (альтернативні джерела) і космос. Саме ці три напрямки цікавили молодого вченого.\n"
                + "В 1995-му Маск відправився в Стенфорд (Stanford), де вчитися в підсумку так і не став, натомість відкривши разом зі своїм братом компанію під назвою 'Zip2'. Компанія була продана в 1999-му, і тоді ж Маск став одним із засновників платіжної системи PayPal. Разом з ним над цим першим сервісом Інтренет-оплати працювали Пітер Тіль і Макс Левчин. В 2002-му сервіс купив Інтернет-аукціон eBay, і після продажу Маск став багатшим на 1,5 мільярда доларів. Так, прийшов час реалізувати свої проекти, пов'язані з космосом і альтернативною енергією. \n"
                + "Відомо, що Елон був справжнім фанатом перспективи колонізації Марсу. Так, існує ідея розміщення на Марсі автоматизованих парників, які з часом могли б стати інкубатором для підтримання власної екосистеми. Втім, вартість затії перевершує всі мислимі межі, а тому він і прийняв рішення розробити власну ракету. \n"
                + "Компанія 'SpaceX' була заснована влітку того ж 2002 року, а в 2006-му був створений ракетоносій 'Falcon 9', запуск якого пройшов вдало. Пізніше в компанії Маска був розроблений космічний корабель 'Dragon', а в 2008-му був укладений контракт з NASA на 12 рейсів на космічну станцію. Сьогодні 'SpaceX' називають символом нової космічної ери - багато очікують від партнерства між урядовим і приватним сектором дуже багатьох чинників. \n"
                + "В 2006-му Маск став співінвестором 'SolarCity' - ця компанія розміщує  сонячні панелі на дахах житлових будинків. \n"
                + "Останнім проектом Ілона Маска стала компанія 'Tesla Motors', в якій він є співзасновником. Компанія ця - стартап із Силіконової долини, яка виробляє електромобілі. Продуктом компанії є автомобіль 'Tesla Roadster'. В 2008-му видання 'Esquire' назвало Маска одним з 75 найвпливовіших людей XXI століття. А журнал 'Forbes' включив Маска в список 20 найбільш впливових CEO у віці до 40 років. \n"
                + "Інтерес для громадськості представляла і продовжує представляти особисте життя Ілона Маска. Його перша дружина, Джастін Маск (Justine Musk), вчилася з ним в одному університеті в Канаді. Вони одружилися в 2000 році, і в сім'ї з'явилося п'ятеро синів. У вересні 2008-го Ілон і Джастін оголосили про сварку, і незабаром стало відомо, що Маск зустрічається з британською актрисою Талулою Райлі (Talulah Riley). В 2012-му стало відомо, що вони розлучилися. \n"
                + "Крім власне бізнесу Маск працює і на благодійність - створений ним фонд фінансує проекти в бідних країнах світу.";

        return poroh;

    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void matchSimplePerson() {

        String text = this.getDefaultText();

        Parser entityParser = new Parser(Person.getGrammarRules(), new ParserTokenPreprocessor[]{
            new DictionaryPreprocessor("Org/Education", "dictionaries/org_education.txt"),
            new DictionaryPreprocessor("Person/Position", "dictionaries/persons.txt")
        });

        LinkedList<String> ll = new LinkedList();

        for (GrammarMatch match : entityParser.resolveMatches(entityParser.extract(text))) {
//            sentences.add(match.toString());

//            if (match.matchedRule.getName().equals("Person_UnnamedPosition")) {
                System.out.println( match.matchedRule.getName()+"[" + match.tokensMatched.get(0).token.position.start + "|" + match + "]");
//            }
//            for (ParserMatch pm : match.tokensMatched) {
////                if (pm.token.forms.size() > 1) {
//                    System.out.println(pm.token.value);
//                
//                    for (TokenForm tf : pm.token.forms) {
//                        System.out.println(tf.grammemes);
//                    }
////                }
//            }
//          
        }   

        System.out.println("Matching done");
    }

    @Test
    public void textToTokens() {

        String text = this.getDefaultText();

        ParserTokenizer tokenizer = ParserTokenizer.defaultTokenizer();
        tokenizer.transform(text);

    }

}
