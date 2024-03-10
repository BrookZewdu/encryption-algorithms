package com.example.application.views;

import com.example.application.views.Developer.DeveloperView;
import com.example.application.views.aes.AESView;
import com.example.application.views.otp.OTPView;
import com.example.application.views.tripledes.TripleDESView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.charts.model.Side;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H2 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("Encryption Algorithms");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        nav.addItem(new SideNavItem("OTP", OTPView.class, LineAwesomeIcon.LOCK_OPEN_SOLID.create()));
        nav.addItem(new SideNavItem("Triple DES", TripleDESView.class, LineAwesomeIcon.UNLOCK_ALT_SOLID.create()));
        nav.addItem(new SideNavItem("AES", AESView.class, LineAwesomeIcon.USER_LOCK_SOLID.create()));
        // nav.addItem(new SideNavItem("Developer", DeveloperView.class,
        // LineAwesomeIcon.CODE_SOLID.create()));

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        // layout.addClassNames(LumoUtility.Padding.Vertical.L,
        // LumoUtility.Padding.Horizontal.L);
        layout.add(new SideNavItem("Developer", DeveloperView.class, LineAwesomeIcon.CODE_SOLID.create()));
        layout.add("CNS 2024");

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
