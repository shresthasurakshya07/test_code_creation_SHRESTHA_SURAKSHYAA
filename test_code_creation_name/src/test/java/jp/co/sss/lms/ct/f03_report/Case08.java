package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.Assert.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

/**
 * 結合テスト レポート機能
 * ケース08
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース08 受講生 レポート修正(週報) 正常系")
public class Case08 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		// トップページURLにアクセス
		goTo("http://localhost:8080/lms");
		// ログイン画面が表示されるまで待つ
		visibilityTimeout(By.xpath("//h2[text()='ログイン']"), 5);
		// ログイン画面であることを検証
		assertEquals("ログイン", webDriver.findElement(By.xpath("//h2[text()='ログイン']")).getText());
		// エビデンス
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		//受生の　ログインIDとパスワードを入力する
		webDriver.findElement(By.id("loginId")).sendKeys("StudentAA02");
		webDriver.findElement(By.id("password")).sendKeys("StudentAA021");
		//ログインボタンを押下//
		webDriver.findElement(By.cssSelector("input[type='submit'][value='ログイン']")).click();

		// コース詳細画面であることを検証
		assertEquals("コース詳細", webDriver.findElement(By.cssSelector("ol.breadcrumb li.active")).getText());

		//エビデンス
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 提出済の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		// 「提出済」の2行目の「詳細」ボタンをクリック
		WebElement detailButton = webDriver.findElement(
				By.xpath("(//tr[contains(normalize-space(.), '提出済')" + " and contains(normalize-space(.), '試験有')])"
						+ "//input[@type='submit' and @value='詳細']"));

		// ボタンが見える位置までスクロール
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block:'center'});", detailButton);
		// クリック
		detailButton.click();
		// セクション詳細画面を待つ
		visibilityTimeout(By.cssSelector("ol.breadcrumb li.active"), 5);

		// セクション詳細画面であることを確認
		assertEquals("セクション詳細", webDriver.findElement(By.cssSelector("ol.breadcrumb li.active")).getText());

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「提出済み週報を確認する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		// 「週報」の「提出済み～を確認する」ボタンを取得
		WebElement confirmButton = webDriver.findElement(By.xpath("//tr[.//input[contains(@value,'週報')]]"
				+ "//input[@type='submit'" + " and contains(@value,'提出済み')" + " and contains(@value,'確認する')]"));

		// ボタンまでスクロール
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block:'center'});", confirmButton);
		// 「提出済み週報を確認する」をクリック
		confirmButton.click();

		// レポート登録画面を待つ
		visibilityTimeout(By.cssSelector("textarea[id^='content_']"), 5);
		// レポート登録画面が表示されたことを確認
		assertTrue(webDriver.findElement(By.cssSelector("textarea[id^='content_']")).isDisplayed());

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しセクション詳細画面に遷移")
	void test05() {

		java.util.List<WebElement> reportContents = webDriver.findElements(
				By.cssSelector("textarea[id^='content_']"));

		// 3つの入力を確認
		assertEquals(3, reportContents.size());
		// 1つ目で1を入力
		reportContents.get(0).clear();
		reportContents.get(0).sendKeys("1");

		// 2つ目で修正1を入力
		reportContents.get(1).clear();
		reportContents.get(1).sendKeys("修正1");

		// 3つ目で修正２を入力
		reportContents.get(2).clear();
		reportContents.get(2).sendKeys("修正２");

		// 「提出する」ボタンを取得
		WebElement submitButton = webDriver
				.findElement(By.xpath("//button[@type='submit' " + "and normalize-space()='提出する']"));

		// ボタンまでスクロール
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block:'center'});", submitButton);

		// 「提出する」をクリック
		submitButton.click();

		// セクション詳細画面に戻るまで待つ
		visibilityTimeout(By.cssSelector("ol.breadcrumb li.active"), 5);

		// セクション詳細画面であることを確認
		assertEquals("セクション詳細", webDriver.findElement(By.cssSelector("ol.breadcrumb li.active")).getText());

		// 提出済みレポートの「確認する」ボタンを取得
		WebElement reportButton = webDriver.findElement(
				By.xpath("//form[contains(@action,'/report/regist')]" + "//input[@type='submit'"
						+ " and contains(@value,'提出済み')" + " and contains(@value,'確認する')]"));

		// 「提出済み○○を確認する」が表示されていることを確認
		assertTrue(reportButton.isDisplayed());

		// エビデンス
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test06() {

		// 「ようこそ○○さん」リンクをクリック
		WebElement user = webDriver.findElement(By.xpath("//a[contains(normalize-space(.),'ようこそ')]"));
		user.click();
		// 「ユーザー詳細」が表示されるまで待つ
		visibilityTimeout(By.xpath("//h2[normalize-space()='ユーザー詳細']"), 5);

		// ユーザー詳細画面であることを確認
		assertEquals("ユーザー詳細", webDriver.findElement(By.xpath("//h2[normalize-space()='ユーザー詳細']")).getText());

		// エビデンス
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 該当レポートの「詳細」ボタンを押下しレポート詳細画面で修正内容が反映される")
	void test07() {
		// TODO ここに追加
	}

}
