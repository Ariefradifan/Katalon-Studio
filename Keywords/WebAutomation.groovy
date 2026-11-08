import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint

import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testcase.TestCaseFactory
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testdata.TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords

import internal.GlobalVariable

import org.openqa.selenium.WebElement
import org.openqa.selenium.WebDriver
import org.openqa.selenium.By

import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.webui.driver.DriverFactory

import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.mobile.helper.MobileElementCommonHelper
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.exception.WebElementNotFoundException


class WebAutomation {

@Keyword
def loginApps(String userName, String password) {
	WebUI.openBrowser(GlobalVariable.url)
	WebUI.maximizeWindow()
	WebUI.click(findTestObject('Object Repository/Element web/01.btnAppoitment'))
	WebUI.verifyElementPresent(findTestObject('Object Repository/Element web/04.btnLogin'), 0)
	WebUI.setText(findTestObject('Object Repository/Element web/02.fldUsername'),userName)
	WebUI.setText(findTestObject('Object Repository/Element web/03.fldPassword'), password)
	WebUI.click(findTestObject('Object Repository/Element web/04.btnLogin'))
	}
	
@Keyword
def setDate(String tanggal, TestObject dateObject) { // format: "15 Dec 2026"
    if (tanggal == null || tanggal.trim() == '') {
        return // tanggal kosong, lewati (untuk negative case)
    }

    String[] dateParts = tanggal.trim().split(" ")
    String day = dateParts[0].toInteger().toString() // "05" -> "5"
    int yearToBe = dateParts[2].toInteger()

    // pembuat locator pilihan (tahun/bulan/tanggal) berdasarkan class view
    def pilihan = { String view, String cls, String teks ->
        TestObject to = new TestObject('pilihan_' + view)
        to.addProperty('xpath', ConditionType.EQUALS,
            "//div[contains(@class,'datepicker-${view}')]//*[contains(@class,'${cls}') and not(contains(@class,'old')) and not(contains(@class,'new')) and normalize-space(text())='${teks}']".toString())
        return to
    }

    WebUI.click(dateObject) // 09.btnCalender, buka kalender
    TestObject title = findTestObject('Object Repository/Element web/10.btnDate')
    WebUI.waitForElementVisible(title, 5)

    String[] currentDates = WebUI.getText(title).trim().split(" ") // "November 2026"
    if (currentDates.size() < 2) {
        KeywordUtil.markFailed("Title kalender tidak terbaca: '" + currentDates.join(' ') + "'")
    }
    int yearCurrent = currentDates[1].toInteger()

    TestObject titleMonths = new TestObject('titleMonths')
    titleMonths.addProperty('xpath', ConditionType.EQUALS, "//div[contains(@class,'datepicker-months')]//th[@class='datepicker-switch']")
    TestObject prevYears = new TestObject('prevYears')
    prevYears.addProperty('xpath', ConditionType.EQUALS, "//div[contains(@class,'datepicker-years')]//th[@class='prev']")
    TestObject nextYears = new TestObject('nextYears')
    nextYears.addProperty('xpath', ConditionType.EQUALS, "//div[contains(@class,'datepicker-years')]//th[@class='next']")

    WebUI.click(title)       // hari -> bulan
    WebUI.click(titleMonths) // bulan -> tahun

    // di view tahun, next/prev = 1 dekade
    int selisih = yearToBe.intdiv(10) - yearCurrent.intdiv(10)
    TestObject navButton = selisih > 0 ? nextYears : prevYears
    for (int n = 0; n < Math.abs(selisih); n++) {
        WebUI.click(navButton)
    }

    WebUI.click(pilihan('years',  'year',  dateParts[2])) // tahun
    WebUI.click(pilihan('months', 'month', dateParts[1])) // bulan
    WebUI.click(pilihan('days',   'day',   day))          // tanggal
 }
 
 @Keyword
 def MakeAppoitment(String Facility, String apply, String healthCare, String Tanggal, String Comment) {
	 WebUI.verifyElementPresent(findTestObject('Object Repository/Element web/05.btnDropdowList'), 0)
	 WebUI.click(findTestObject('Object Repository/Element web/05.btnDropdowList'))
	 WebUI.click(findTestObject('Object Repository/Element web/06.btnListddl',[('label') : Facility]))
	 if(apply == 'Y') {
		 WebUI.click(findTestObject('Object Repository/Element web/07.btnChekbox'))
	 }
	 WebUI.click(findTestObject('Object Repository/Element web/08.btnRadio',[('label') : healthCare]))
	 TestObject dateObjectVisit = findTestObject('Object Repository/Element web/09.btnCalender')
	 CustomKeywords.'WebAutomation.setDate'(Tanggal, dateObjectVisit)
	 WebUI.setText(findTestObject('Object Repository/Element web/14.fldComment'), Comment)
	 WebUI.takeScreenshot()
	 WebUI.click(findTestObject('Object Repository/Element web/15.btnAppointment'))
	 WebUI.takeScreenshot()
	 WebUI.click(findTestObject('Object Repository/Element web/16.btnHomePage'))
 }
}