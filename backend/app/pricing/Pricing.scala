package pricing

import java.time.{Duration, Instant, LocalDate, LocalTime, ZoneId, ZonedDateTime}

object Pricing {
  val peakPricePerUnit: BigDecimal = BigDecimal("0.005")
  val offPeakPricePerUnit: BigDecimal = BigDecimal("0.004")
  private val milisInHour = 3600000
  private val zoneId: ZoneId = ZoneId.of("UTC") // using single zone for simplicity

  def calculateCost(
      startInstant: Instant,
      endInstant: Instant,
      chargingSpeed: Double
  ): CostBreakdown = {
    // break down days into start&end for each day
    val startAndEndPerDayList = splitDurationByDay(startInstant, endInstant)

    // for each day now we should calculate a UnitsUsedPerDate
    val unitsUsedPerDate = startAndEndPerDayList.map(startAndEnd => {
      getUnitsUsedPerDay(startAndEnd, chargingSpeed)
    })

    val totalOffPeakUnits = unitsUsedPerDate.map(du => du.offPeakMorning + du.offPeakNight).sum
    val totalPeakUnits = unitsUsedPerDate.map(du => du.peak).sum

    val offPeakCost = totalOffPeakUnits * offPeakPricePerUnit
    val peakCost = totalPeakUnits * peakPricePerUnit

    CostBreakdown(
      totalOffPeakUnits,
      totalPeakUnits,
      totalOffPeakUnits + totalPeakUnits,
      offPeakCost,
      peakCost,
      offPeakCost + peakCost
    )

  }

  private def splitDurationByDay(
      startInstant: Instant,
      endInstant: Instant
  ): List[PerDayStartAndEnd] = {
    // need zoned date
    val startZonedDate = startInstant.atZone(zoneId)
    val startDate = startZonedDate.toLocalDate

    val endZonedDate = endInstant.atZone(zoneId)
    val endDate = endZonedDate.toLocalDate

    // iterator using plusDays until we run out of days
    val dates = Iterator
      .iterate(startDate)(_.plusDays(1))
      .takeWhile(!_.isAfter(endDate))

    dates.map { date =>
      val dayStart = if (date == startDate) startZonedDate else date.atStartOfDay(zoneId)
      val dayEnd =
        if (date == endDate) endZonedDate
        else date.plusDays(1).atStartOfDay(zoneId) // gets us to midnight

      PerDayStartAndEnd(dayStart, dayEnd)
    }.toList
  }

  private def getUnitsUsedPerDay(
      day: PerDayStartAndEnd,
      chargingSpeed: Double
  ): UnitsUsedPerDate = {
    // will work in hours - get hours for each of the three time blocks
    val date = day.startTime.toLocalDate
    val dayStart = date.atStartOfDay(zoneId)
    val peakStart = date.atTime(8, 0).atZone(zoneId)
    val peakEnd = date.atTime(20, 0).atZone(zoneId)
    val nextDayStart = date.plusDays(1).atStartOfDay(zoneId)

    val unitsOffPeakMorning = getHoursPerBlock(day, dayStart, peakStart) * chargingSpeed
    val unitsPeak = getHoursPerBlock(day,peakStart, peakEnd) * chargingSpeed
    val unitsOffPeakNight = getHoursPerBlock(day, peakEnd, nextDayStart) * chargingSpeed

    UnitsUsedPerDate(
      unitsOffPeakMorning,
      unitsPeak,
      unitsOffPeakNight
    )
  }

  private def getHoursPerBlock(day: PerDayStartAndEnd, blockStart: ZonedDateTime, blockEnd: ZonedDateTime): Double = {
    val effectiveStartForBlock = if(day.startTime.isAfter(blockStart)) day.startTime else blockStart
    val effectiveEndForBlock = if (day.endTime.isBefore(blockEnd)) day.endTime else blockEnd
    if (effectiveStartForBlock.isBefore(effectiveEndForBlock))
      Duration.between(effectiveStartForBlock, effectiveEndForBlock).toMillis.toDouble / milisInHour
    else 0.0
  }

}

case class UnitsUsedPerDate(offPeakMorning: Double, peak: Double, offPeakNight: Double)

case class PerDayStartAndEnd(startTime: ZonedDateTime, endTime: ZonedDateTime)

case class CostBreakdown(
    totalOffPeakUnits: Double,
    totalPeakUnits: Double,
    totalUnits: Double,
    offPeakCost: BigDecimal,
    peakCost: BigDecimal,
    totalCost: BigDecimal
)
