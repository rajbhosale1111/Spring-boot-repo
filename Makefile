
.DEFAULT_GOAL := build

# Set default shell behavior
SHELL := bash
.SHELLFLAGS := -eu -o pipefail -c

# load ENV vars from .env.local if it exists
ifneq ("$(wildcard .env.local)","")
include .env.local
export $(shell cat .env.local | grep -v '^\#' | sed 's/=.*//' )
endif

SKIP_TESTS := -Dmaven.test.skip=true -DskipTests=true
SPRING_PROFILE := local

# Set this to pass extra options while running spring server
JAVA_OPTS ?=

# Use for passing extra options to maven while running all targets
MVN_OPTS := $(MVN_OPTS) -Dorg.slf4j.simpleLogger.log.io.swagger.codegen=WARN

check-config:
	@if [ ! -f .env.local ]; then \
		echo ".env.local not found"; \
	  exit 1; \
	fi;
	@_s=$$(cat .env.sample | grep -ve '^ *#' | grep -v '^$$' | wc -l); \
	_l=$$(cat .env.local | grep -ve '^ *#' | grep -v '^$$' | wc -l); \
	if [ $$_l -lt $$_s ]; then \
		echo "ERROR: mismatch between env files:"; \
	  echo "  .env.sample: $$_s vars"; \
	  echo "  .env.local: $$_l vars"; \
		echo ; \
		echo "  try running vimdiff .env.local .env.sample"; \
		exit 1; \
  fi;

check: check-config

# Just print the local env config vars
env:
	cat .env.local | grep -v '^ *#' | grep -v '^$$'

clean-backend:
	./mvnw $(MVN_OPTS) $ --projects=backend clean

clean-frontend-cache:
	rm -f .yarn.cache
	rm -rf frontend/node_modules/.vite/
	rm -rf frontend/node_modules/.cache/
	rm -rf frontend/tests/unit/coverage/
	rm -rf frontend/temp/

clean-frontend:
	rm -rf frontend/tests/unit/coverage/
	./mvnw $(MVN_OPTS) --projects=frontend clean

clean: clean-backend clean-frontend-cache
	#./mvnw $(MVN_OPTS) clean

#build-docker: build
#	docker build -t kickstart-web:latest .

#build-docker-cloudbuild:
#	docker build --build-arg SSH_KEY="$$(cat ~/.ssh/id_ed25519)" -f Dockerfile.cloudbuild -t kickstart-web:latest .

build-backend:
	./mvnw $(MVN_OPTS) $(SKIP_TESTS) --projects=backend install

build-frontend:
	./mvnw $(MVN_OPTS) $(SKIP_TESTS) --projects=frontend install

build:
	./mvnw $(MVN_OPTS) $(SKIP_TESTS) --batch-mode --no-transfer-progress install

ci-build:
	./mvnw --batch-mode --no-transfer-progress clean install

package: build

test-backend:
	./mvnw $(MVN_OPTS) --projects=backend test

test-frontend:
	./mvnw $(MVN_OPTS) --projects=frontend test

test:
	./mvnw $(MVN_OPTS) test

kill-spring:
	@_pid=$$(pgrep -f "java.*MainApiApplication.*spring-boot:run"); \
	if [ -n "$$_pid" ]; then \
		echo "killing $$_pid"; \
		kill $$_pid;\
	fi;

spring-running:
	@_pid=$$(pgrep -f "java.*MainApiApplication.*spring-boot:run"); \
	if [ -n "$$_pid" ]; then \
		echo "ERROR: spring already running at $$_pid"; \
		exit 2; \
	else \
		echo "spring not running"; \
	fi;


run-backend: kill-spring spring-running check
	./mvnw $(MVN_OPTS) $(JAVA_OPTS) $(SKIP_TESTS) \
		-Dspring-boot.run.profiles=$(SPRING_PROFILE) \
		-Dspring-boot.run.main-class=com.at.MainApiApplication \
		--projects backend \
		spring-boot:run

watch: kill-spring spring-running
	if command -v watchexec >/dev/null; then \
		#watchexec --no-default-ignore --no-ignore --no-vcs-ignore --restart --watch .git/HEAD 'vproxy client --bind platform.local.com:8088 -- make clean spring'; \
		watchexec --no-default-ignore --no-ignore --no-vcs-ignore --restart --watch .git/HEAD 'make clean spring'; \
	else \
		make clean spring; \
	fi;

vproxy:
	vproxy client --bind platform.local.com:8088

spring: run-backend

run-jar:
	java -jar backend/target/*.jar

#pw:
#	./mvnw \
#		--quiet \
#		--projects backend \
#		compile \
#		exec:java \
#		-Dstart-class=com.digitalremedy.kickstart.web.cli.PasswordEncoderApp
#
#jwt:
#	./mvnw \
#		--quiet \
#		--projects backend \
#		compile \
#		exec:java \
#		-Dstart-class=com.digitalremedy.kickstart.web.cli.JwtGenApp

kill-vue:
	@cd frontend; \
	_pid=$$(pgrep -f "$$PWD/node_modules/.bin/vite"); \
	if [ -n "$$_pid" ]; then \
		echo "killing $$_pid"; \
		kill $$_pid;\
	fi;

vue-running:
	@cd frontend; \
	_pid=$$(pgrep -f "$$PWD/node_modules/.bin/vite"); \
	if [ -n "$$_pid" ]; then \
		echo "ERROR: vue already running at $$_pid"; \
		exit 2; \
	else \
		echo "vue not running"; \
	fi;

run-frontend: kill-vue vue-running check yarn-if-needed
	cd frontend; \
		rm -rf temp; \
		if command -v watchexec >/dev/null; then \
			watchexec --restart --watch yarn.lock 'yarn serve'; \
		else \
			yarn serve; \
		fi;

run-frontend: kill-vue vue-running check yarn-if-needed
	cd frontend; \
		rm -rf temp; \
		if command -v watchexec >/dev/null; then \
			watchexec --restart --watch yarn.lock 'vproxy client --bind vite.local.com:8083 --bind platform.local.com:8088 -- yarn serve'; \
		else \
			yarn serve; \
		fi;

run-frontend1: kill-vue vue-running check yarn-if-needed
	cd frontend; \
		rm -rf temp; \
		if command -v watchexec >/dev/null; then \
			watchexec --restart --watch yarn.lock 'yarn serve'; \
		else \
			yarn serve; \
		fi;

#run-vue: run-frontend

vue: run-frontend

yarn: check
	cd frontend && yarn

# Only run yarn install if the lock file changed
yarn-if-needed: check
	@shasum --check .yarn.cache; \
	if [ $$? -ne 0 ]; then \
		rm -f .yarn.cache; \
		make yarn; \
		shasum -a 256 frontend/yarn.lock > .yarn.cache; \
	fi;

yarn-upgrade: check
	cd frontend && yarn upgrade-interactive

yarn-upgrade-latest: check
	cd frontend && yarn upgrade-interactive --latest

#run-storybook: check yarn
#	cd frontend; \
#		if command -v watchexec >/dev/null; then \
#			watchexec --restart --watch yarn.lock 'yarn install; yarn run storybook'; \
#		else \
#			yarn storybook; \
#		fi;
#
#storybook: run-storybook

new-migration:
	echo; set -e; \
	read -p "Enter migration file name (e.g., RP-1000-add-foo-col): " filename; \
	f="backend/src/main/resources/db/migration/V$$(date -u +%Y%m%d%H%M%S)__$${filename}.sql"; \
	touch $$f; \
	echo "created new migration: $$f";

# it's all a dream
# egrep ':$' Makefile | tr -d : | tr '\n' ' '
.SILENT:
.PHONY: clean-backend clean-frontend build-backend build-frontend test-backend test-frontend run-backend spring run-frontend yarn yarn-upgrade vue-running spring-running yarn-if-needed
