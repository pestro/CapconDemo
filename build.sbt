name := "CapconDemo"

version := "0.1"

scalaVersion := "2.11.12"

scalacOptions += "-Ypartial-unification" // 2.11.9+

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % "2.0.0",
  "com.chuusai" %% "shapeless" % "2.3.3",

  // Start with this one
  "org.tpolecat" %% "doobie-core"      % "0.7.0",

  // And add any of these as needed
  "org.tpolecat" %% "doobie-h2"        % "0.7.0",          // H2 driver 1.4.199 + type mappings.
  "org.tpolecat" %% "doobie-hikari"    % "0.7.0",          // HikariCP transactor.
  "org.tpolecat" %% "doobie-postgres"  % "0.7.0",          // Postgres driver 42.2.5 + type mappings.
  "org.tpolecat" %% "doobie-quill"     % "0.7.0",          // Support for Quill 3.1.0
  "org.tpolecat" %% "doobie-specs2"    % "0.7.0" % "test", // Specs2 support for typechecking statements.
  "org.tpolecat" %% "doobie-scalatest" % "0.7.0" % "test"  // ScalaTest support for typechecking statements.

)