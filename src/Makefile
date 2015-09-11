OBJECTS = \
  com/kjwork/cryptofile/PublicKeyEncapsulationError.class \
  com/kjwork/cryptofile/PublicKeyEncapsulatedInputStream.class \
  com/kjwork/cryptofile/PublicKeyEncapsulatedOutputStream.class \
  com/kjwork/cryptofile/Util.class \
  com/kjwork/data/PrivKeyStore.class \
  com/kjwork/data/PubKeyDatabase.class \
  com/kjwork/data/PubKeyDbEnumeration.class \
  com/kjwork/data/PubKeyRecord.class \
  com/kjwork/gui/CFDecPane.class \
  com/kjwork/gui/CFEncPane.class \
  com/kjwork/gui/CFKeyPane.class \
  com/kjwork/gui/CFPrvPane.class \
  com/kjwork/gui/CFSecPane.class \
  com/kjwork/gui/CFRndPane.class \
  com/kjwork/gui/CFMenu.class \
  com/kjwork/gui/CFHelpWindow.class \
  com/kjwork/gui/PubKeyImportDialog.class \
  com/kjwork/gui/MainWindow.class \
  com/kjwork/gui/Main.class \
  com/kjwork/util/Base64.class \
  com/kjwork/util/FileGrab.class \

ALLCLASSES = $(OBJECTS) \

ALLRESOURCES = $(ALLCLASSES) \
  com/kjwork/help/enc.txt \

JAR = jar
JAVA = java
JAVAC = javac

FLAGS = -Xlint:deprecation -Xlint:unchecked

TARGET = cryptofile.jar

all: $(TARGET)

clean:
	@/bin/echo "  [CLEAN]"; rm -f $(TARGET) $(ALLCLASSES)

run: all
	java com/kjwork/gui/Main

test: all com/kjwork/cryptofile/test/TestPublicKeyEncapsulation.class
	@$(JAVA) -classpath $(TARGET):. com/kjwork/cryptofile/test/TestPublicKeyEncapsulation

run: all
	@$(JAVA) -jar $(TARGET)

$(TARGET): $(OBJECTS)
	@/bin/echo -n "  [JAR ] $(TARGET) .. "; \
	if $(JAR) -cfe $(TARGET) com.kjwork.gui.Main $(ALLRESOURCES); then \
	  chmod 775 $(TARGET); \
	  echo "Ok"; \
	else \
	  echo "Failed: $(JAR) -cfe $(TARGET) com.kjwork.gui.Main $(ALLCLASSES)"; \
	  exit -1; \
	fi

%.class: %.java
	@CLASS=`basename $@`; \
	/bin/echo -n "  [JAVAC] $$CLASS .. "; \
	if $(JAVAC) $(FLAGS) $<; then \
	  echo "Ok"; \
	else \
	  echo "Failed: $(JAVAC) $(FLAGS) $<"; \
	  exit -1; \
	fi
