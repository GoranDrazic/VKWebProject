package com.vk.tottenham.template;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.vk.tottenham.core.model.Page;
import com.vk.tottenham.footballapi.model.CompetitionName;
import com.vk.tottenham.mybatis.service.DictionaryService;
import com.vk.tottenham.utils.CompSeasonUtil;

public abstract class TemplateBase {
    private static final String LOWER_MENU_TEMPLATE = "<center>"
            + "[[##PREMIER_LEAGUE_PHOTO##|88px;noborder;nopadding|##PREMIER_LEAGUE_PAGE##]]"
            + "[[##FA_CUP_PHOTO##|88px;noborder;nopadding|##FA_CUP_PAGE##]]"
            + "[[##EFL_CUP_PHOTO##|88px;noborder;nopadding|##EFL_CUP_PAGE##]]"
            + "[[##UEFA_CHAMPIONS_LEAGUE_PHOTO##|88px;noborder;nopadding|##UEFA_CHAMPIONS_LEAGUE_PAGE##]]"
            + "[[##UEFA_EUROPA_LEAGUE_PHOTO##|88px;noborder;nopadding|##UEFA_EUROPA_LEAGUE_PAGE##]]"
            + "</center>";

    private static final String CAPTION_TEMPLATE = 
            "{|\n|-\n! <center><gray>##competition## ##season##</gray></center>\n|}";

    private static final String FOOTER_ICON_TEMPLATE = "[[##PHOTO##|145px;noborder;nopadding|##PAGE##]]";

    @Autowired
    @Qualifier("dictionaryService")
    protected DictionaryService dictionaryService;

    public abstract boolean isUpdateNeeded(PageContext context);

    public abstract String buildContent(PageContext context);
    
    protected boolean shouldAddFooter (PageContext context) {
        return true;
    }

    public String build(PageContext context) throws IOException {
        URL url = Resources.getResource(
                "com/vk/tottenham/contents/pageContent.txt");
        String pageContents = Resources.toString(url, Charsets.UTF_8);

        pageContents = pageContents.replace("##lowerHeader##",
                buildHeader(context));
        pageContents = pageContents.replace("##pageCaption##",
                buildCaption(context));
        pageContents = pageContents.replace("##linkSection##",
                buildLinks(context));
        pageContents = pageContents.replace("##content##",
                buildContent(context));
        if (shouldAddFooter(context)) {
            pageContents = pageContents.concat(buildFooter(context));
        }
        return pageContents;
    }

    private String buildFooter(PageContext context) {
        StringBuilder builder = new StringBuilder();
        List<String> icons = new LinkedList<>();
        for (Year year : Year.values()) {
            Page page = context.getFooterPages().get(year);
            if (page != null) {
                icons.add(new String(FOOTER_ICON_TEMPLATE)
                        .replace("##PHOTO##", year.icon())
                        .replace("##PAGE##", page.getPageLink()));
            }
            if (icons.size() == 3) {
                builder.append("<center>").append(String.join(" ", icons))
                        .append("</center>\n\n");
                icons.clear();
            }
        }
        if (icons.size() > 0) {
            builder.append("<center>").append(String.join(" ", icons))
                    .append("</center>\n\n");
            icons.clear();
        }
        return builder.toString().trim();
    }

    private CharSequence buildCaption(PageContext context) {
        String result = new String(CAPTION_TEMPLATE);
        result = result.replace("##competition##", dictionaryService.translateInRussianNom(context.getCompetition().value()))
                .replace("##season##", CompSeasonUtil.getSeasonFull());
        return result;
    }

    private String buildLinks(PageContext context) {
        List<String> links = new LinkedList<>();
        for (PageType pageType : PageType.values()) {
            Page link = context.getLinks().get(pageType);
            if (link == null) continue;
            if (pageType == context.getPageType()) {
                links.add("'''[[" + link.getPageLink() + "|" + dictionaryService.translateInRussianNom(link.getPageType().caption()) + "]]'''");
            } else {
                links.add("[[" + link.getPageLink() + "|" + dictionaryService.translateInRussianNom(link.getPageType().caption()) + "]]");
            }
        }
        return "<center>" + String.join(" | ", links) + "</center>";
    }

    private String buildHeader(PageContext context) {
        String lowerMenu = new String(LOWER_MENU_TEMPLATE);
        for (CompetitionName competition : CompetitionName.values()) {
            if (competition == context.getCompetition()) {
                lowerMenu = lowerMenu.replace("##" + competition + "_PHOTO##", competition.activeIcon());
            } else {
                lowerMenu = lowerMenu.replace("##" + competition + "_PHOTO##", competition.inactiveIcon());
            }
            lowerMenu = lowerMenu.replace("##" + competition + "_PAGE##", competition.firstPage());
        }
        return lowerMenu;
    }
}
