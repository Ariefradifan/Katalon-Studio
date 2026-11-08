import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.llm.keyword.LlmKeywords as LLM
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

data = findTestData('Data Files/WebAutomation/WebAutomation')

for (int i = 1; i <= data.getRowNumbers(); i++) {
	String userName   = data.getValue('userName',i)
	String password   = data.getValue('password', i)
	String Facility   = data.getValue('Facility', i)
	String apply      = data.getValue('Apply', i)
	String healthCare = data.getValue('healthCare', i)
	String Tanggal    = data.getValue('Tanggal', i)
	String Comment    = data.getValue('Comment', i)

	
	WebUI.openBrowser(GlobalVariable.url)
	WebUI.click(findTestObject('Object Repository/Element web/01.btnAppoitment'))
	WebUI.verifyElementPresent(findTestObject('Object Repository/Element web/04.btnLogin'), 0)
	WebUI.setText(findTestObject('Object Repository/Element web/02.fldUsername'),userName)
	WebUI.setText(findTestObject('Object Repository/Element web/03.fldPassword'), password)
	WebUI.click(findTestObject('Object Repository/Element web/04.btnLogin'))
	
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

