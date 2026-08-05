package pricing

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.time.{Instant, LocalDateTime, ZoneId}

class PricingSpec extends AnyFlatSpec with Matchers {
  private val utc = ZoneId.of("UTC")

  private def instantAt(date: String, time: String): Instant =
    LocalDateTime.parse(s"${date}T$time").atZone(utc).toInstant


  "simpleCalculateCostPeak" should "charge peak rate when only charging during peak hours" in {
    val result = Pricing.calculateCost(instantAt("2026-08-05", "09:00:00"), instantAt("2026-08-05", "11:00:00"), chargingSpeed = 50.0)
    result.totalPeakUnits shouldBe 100.0
    result.totalOffPeakUnits shouldBe 0.0
    result.peakCost shouldBe BigDecimal("0.5")
    result.offPeakCost shouldBe BigDecimal("0")
  }

  "simpleCalculateTotalPeak" should "charge peak rate when charging during ALL peak hours" in {
    val result = Pricing.calculateCost(instantAt("2026-08-05", "08:00:00"), instantAt("2026-08-05", "20:00:00"), chargingSpeed = 100.0)
    result.totalPeakUnits shouldBe 1200.0
    result.totalOffPeakUnits shouldBe 0.0
    result.peakCost shouldBe BigDecimal("6.0")
    result.offPeakCost shouldBe BigDecimal("0")
  }

  "simpleCalculateCostOffPeak" should "charge off peak rate when only charging during off peak hours" in {
    val result = Pricing.calculateCost(instantAt("2026-08-05", "05:00:00"), instantAt("2026-08-05", "07:00:00"), chargingSpeed = 50.0)
    result.totalPeakUnits shouldBe 0.0
    result.totalOffPeakUnits shouldBe 100.0
    result.peakCost shouldBe BigDecimal("0")
    result.offPeakCost shouldBe BigDecimal("0.4")
  }

  "checkUnits" should "ensure unit values are correct" in {
    val result = Pricing.calculateCost(instantAt("2026-08-05", "07:00:00"), instantAt("2026-08-05", "09:00:00"), chargingSpeed = 50.0)
    result.totalPeakUnits + result.totalOffPeakUnits shouldBe 50*2
  }

  "offPeaktoPeak" should "charge for both peak and off peak at appropriate rate" in {
    val result = Pricing.calculateCost(instantAt("2026-08-05", "07:00:00"), instantAt("2026-08-05", "09:00:00"), chargingSpeed = 100.0)
    result.totalPeakUnits shouldBe 100.0
    result.totalOffPeakUnits shouldBe 100.0
    result.peakCost shouldBe BigDecimal("0.5")
    result.offPeakCost shouldBe BigDecimal("0.4")
  }

  "wholeDay" should "charge appropriate amount for a full day" in {
    val result = Pricing.calculateCost(instantAt("2026-08-05", "00:00:00"), instantAt("2026-08-06", "00:00:00"), chargingSpeed = 100.0)
    result.totalPeakUnits shouldBe 1200.0
    result.totalOffPeakUnits shouldBe 1200.0
    result.peakCost shouldBe BigDecimal("6.0")
    result.offPeakCost shouldBe BigDecimal("4.8")
  }

  "middayToTomorrowMidday" should "charge appropriate amount for a full day starting at midday" in {
    val result = Pricing.calculateCost(instantAt("2026-08-05", "12:00:00"), instantAt("2026-08-06", "12:00:00"), chargingSpeed = 100.0)
    result.totalPeakUnits shouldBe 1200.0
    result.totalOffPeakUnits shouldBe 1200.0
    result.peakCost shouldBe BigDecimal("6.0")
    result.offPeakCost shouldBe BigDecimal("4.8")
  }

  "elevenDays" should "charge for 11 full days usage" in {
    val result = Pricing.calculateCost(instantAt("2026-08-05", "00:00:00"), instantAt("2026-08-16", "00:00:00"), chargingSpeed = 100.0)
    result.totalPeakUnits shouldBe 13200.0
    result.totalOffPeakUnits shouldBe 13200.0
    result.peakCost shouldBe BigDecimal("66.0")
    result.offPeakCost shouldBe BigDecimal("52.8")
  }

  "noDuration" should "not charge anything as start = end" in {
    val result = Pricing.calculateCost(instantAt("2026-08-05", "09:00:00"), instantAt("2026-08-05", "09:00:00"), chargingSpeed = 50.0)
    result.totalPeakUnits shouldBe 0.0
    result.totalOffPeakUnits shouldBe 0.0
    result.peakCost shouldBe BigDecimal("0")
    result.offPeakCost shouldBe BigDecimal("0")
  }


}
